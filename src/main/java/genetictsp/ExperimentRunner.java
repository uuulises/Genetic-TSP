package genetictsp;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

import genetictsp.algorithm.crossover.*;
import genetictsp.algorithm.crossover.implementations.*;
import genetictsp.algorithm.mutation.*;
import genetictsp.algorithm.mutation.implementations.*;
import genetictsp.algorithm.selection.parent.ParentSelection;
import genetictsp.algorithm.selection.parent.implementations.*;
import genetictsp.algorithm.selection.survivor.SurvivorSelection;
import genetictsp.algorithm.selection.survivor.implementations.*;
import genetictsp.algorithm.FitnessCalculator;
import genetictsp.algorithm.GeneticAlgorithm;
import genetictsp.model.Costs;
import genetictsp.util.InstanceLoader;

public class ExperimentRunner {
    private final static String INSTANCE_FILE = "data/br17.atsp";
    private final static int RUNS_PER_CONFIG = 4;

    // Clase interna para trackear resultados y calcular promedios
    static class ConfigResult {
        String configID;
        List<Double> fitnesses = new ArrayList<>();
        long totalTime;

        ConfigResult(String id) { this.configID = id; }
        
        double getAverage() {
            return fitnesses.stream().mapToDouble(a -> a).average().orElse(0.0);
        }
        
        double getStdDev() {
            double avg = getAverage();
            double sum = 0;
            for (double f : fitnesses) sum += Math.pow(f - avg, 2);
            return Math.sqrt(sum / fitnesses.size());
        }
    }

    public static void main(String[] args) {
        String csvFile = "results/TSP_Full_Experiments.csv";
        Map<String, ConfigResult> statsMap = new LinkedHashMap<>();

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            // 1. Cabecera del CSV
            writer.println("Configuration;ParentSelection;Crossover;Mutation;SurvivorSelection;PopulationSize;MaxGen;Run;BestFitness;TimeMS");

            InstanceLoader.Instance instance = InstanceLoader.loadATSP(INSTANCE_FILE);
            int cities = instance.dimension;
            Costs.resetInstance();
            Costs.getInstance(instance.costMatrix);

            // 2. Definición de operadores y complementos, junto con los parámetros pertinentes
            List<ParentSelection> parentSelections = Arrays.asList(new RouletteWheelSelection(), new TournamentSelection(1)); // Cambiamos el tamaño del torneo luego, proporcionalmente al tamaño de la población
            List<Crossover> crossovers = Arrays.asList(
                new OrderCrossover(0.6), 
                new OrderCrossover(0.8),
                new PartiallyMappedCrossover(0.6),
                new PartiallyMappedCrossover(0.8)
            );
            List<Mutation> mutations = Arrays.asList(
                new SwapMutation(0.1), 
                new SwapMutation(0.3),
                new ReversalMutation(0.1),
                new ReversalMutation(0.3));
            List<SurvivorSelection> survivorSelections = Arrays.asList(new FullGenReplacement(), new ElitismBasedReplacement(), new SteadyStateReplacement(1));

            // 3. Parámetros 
            int[] populationSizes = {100, 200};
            int[] maxGenerations = {1000, 2000};
            int[] maxConsecutiveFitnessesWithoutImprovement = {50, 200};

            System.out.println("Iniciando batería de tests...");

            for (int popSize : populationSizes) {
                int tournamentSize = Math.max(2, popSize / 10); // El 10% de la población, mínimo 2
                int steadyStateSize = Math.max(1, popSize / 2); // El 50% de la población, mínimo 1
                for (int maxGen : maxGenerations) {
                    for (int maxNoImprove : maxConsecutiveFitnessesWithoutImprovement) {
                        for (ParentSelection sel : parentSelections) {
                            for (Crossover cross : crossovers) {
                                for (Mutation mut : mutations) {
                                    for (SurvivorSelection surv : survivorSelections) {
                                        String selName;
                                        if (sel instanceof TournamentSelection) {
                                            ((TournamentSelection) sel).setTournamentSize(tournamentSize);
                                            selName = "Tournament_Size_" + tournamentSize;
                                        } else {
                                            selName = "Roulette";
                                        }
                                        if (surv instanceof SteadyStateReplacement) {
                                            ((SteadyStateReplacement) surv).setNumToReplace(steadyStateSize);
                                        }

                                        String configID = String.format("%s+%s+%s+%s+P%d", 
                                            selName, cross.getClass().getSimpleName(), mut.getClass().getSimpleName(), 
                                            surv.getClass().getSimpleName(), popSize);
                                        
                                        ConfigResult stat = new ConfigResult(configID);

                                        for (int run = 1; run <= RUNS_PER_CONFIG; run++) {
                                            GeneticAlgorithm ga = new GeneticAlgorithm(sel, cross, mut, surv);
                                            
                                            long startTime = System.currentTimeMillis();
                                            
                                            ArrayList<int[]> result = ga.run(maxGen, maxNoImprove, popSize, cities);
                                            long endTime = System.currentTimeMillis();
                                            
                                            double bestFit = FitnessCalculator.calculate(result.get(0));
                                            
                                            // Guardar fila individual
                                            writer.printf("%s;%s;%s;%s;%s;%d;%d;%d;%.2f;%d%n",
                                                configID, selName, cross.getClass().getSimpleName(), 
                                                mut.getClass().getSimpleName(), surv.getClass().getSimpleName(),
                                                popSize, maxGen, run, bestFit, (endTime - startTime));
                                            
                                            stat.fitnesses.add(bestFit);
                                            stat.totalTime += (endTime - startTime);
                                        }
                                        statsMap.put(configID, stat);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. SECCIÓN DE ANÁLISIS FINAL (Al final del mismo Excel)
            writer.println("\n\n;=== RESUMEN ESTADISTICO POR CONFIGURACION ===;");
            writer.println("ConfigID;Promedio Fitness;Desviacion Estandar;Mejor Historico;Tiempo Promedio MS");
            
            for (ConfigResult res : statsMap.values()) {
                double mejor = res.fitnesses.stream().mapToDouble(d -> d).min().orElse(0);
                writer.printf("%s;%.2f;%.2f;%.2f;%d%n", 
                    res.configID, res.getAverage(), res.getStdDev(), mejor, res.totalTime / RUNS_PER_CONFIG);
            }

            System.out.println("Proceso terminado. Archivo generado: " + csvFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}