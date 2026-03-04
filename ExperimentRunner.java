import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

import General.*;
import Crossover.*;
import Crossover.implementations.*;
import Mutation.*;
import Mutation.implementations.*;
import Selection.*;
import Selection.implementations.*;
import SurvivorSelection.*;
import SurvivorSelection.implementations.*;

public class ExperimentRunner {
    private final static String INSTANCE_FILE = "instancias-TSP/br17.atsp";
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
        String csvFile = "TSP_Full_Experiments.csv";
        Map<String, ConfigResult> statsMap = new LinkedHashMap<>();

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            // 1. Cabecera detallada
            writer.println("ConfigID;Seleccion;Crossover;Mutacion;Supervivencia;PopSize;MaxGen;Run;BestFitness;TimeMS");

            InstanceLoader.Instance instance = InstanceLoader.loadATSP(INSTANCE_FILE);
            int cities = instance.dimension;
            Costs.resetInstance();
            Costs.getInstance(instance.costMatrix);

            // 2. Definición de Implementaciones (Aquí incluyo varios Tournament)
            List<Selection> selections = Arrays.asList(
                new RouletteWheelSelection(), 
                new TournamentSelection(3),   // Torneo pequeño
                new TournamentSelection(10),  // Torneo medio
                new TournamentSelection(25)   // Torneo grande
            );
            List<Crossover> crossovers = Arrays.asList(new OrderCrossover(), new PartiallyMappedCrossover());
            List<Mutation> mutations = Arrays.asList(new SwapMutation(), new ReversalMutation());
            List<SurvivorSelection> survivors = Arrays.asList(new FullGenReplacement(), new SteadyStateSurvivorSelection());

            // 3. Parámetros de Población
            int[][] parameterSets = {{100, 1000}, {200, 2000}};

            System.out.println("Iniciando batería de tests...");

            for (int[] params : parameterSets) {
                for (Selection sel : selections) {
                    for (Crossover cross : crossovers) {
                        for (Mutation mut : mutations) {
                            for (SurvivorSelection surv : survivors) {
                                
                                // Identificador único para este grupo de 4 corridas
                                String selName = (sel instanceof TournamentSelection) ? "Tournament_Size_" + getTournamentSize(sel) : "Roulette";
                                String configID = String.format("%s+%s+%s+%s+P%d", 
                                    selName, cross.getClass().getSimpleName(), mut.getClass().getSimpleName(), 
                                    surv.getClass().getSimpleName(), params[0]);
                                
                                ConfigResult stat = new ConfigResult(configID);

                                for (int run = 1; run <= RUNS_PER_CONFIG; run++) {
                                    GeneticAlgorithm ga = new GeneticAlgorithm(sel, cross, mut, surv);
                                    ga.initializeComponents(cities);
                                    
                                    long startTime = System.currentTimeMillis();
                                    ArrayList<int[]> pop = ga.initializePopulation(params[0], cities);
                                    double[] fitness = ga.evaluatePopulation(params[0], pop);
                                    
                                    ArrayList<int[]> result = ga.run(params[1], 80, pop, fitness);
                                    long endTime = System.currentTimeMillis();
                                    
                                    double bestFit = ga.calculateFitness(result.get(0));
                                    
                                    // Guardar fila individual
                                    writer.printf("%s;%s;%s;%s;%s;%d;%d;%d;%.2f;%d%n",
                                        configID, selName, cross.getClass().getSimpleName(), 
                                        mut.getClass().getSimpleName(), surv.getClass().getSimpleName(),
                                        params[0], params[1], run, bestFit, (endTime - startTime));
                                    
                                    stat.fitnesses.add(bestFit);
                                    stat.totalTime += (endTime - startTime);
                                }
                                statsMap.put(configID, stat);
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

    // Helper para extraer el tamaño del torneo por Reflection (ya que es privado en tu clase)
    private static int getTournamentSize(Selection sel) {
        try {
            java.lang.reflect.Field field = sel.getClass().getDeclaredField("tournamentSize");
            field.setAccessible(true);
            return (int) field.get(sel);
        } catch (Exception e) { return 0; }
    }
}