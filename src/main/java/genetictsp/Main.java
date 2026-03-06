package genetictsp;
import java.util.ArrayList;

import genetictsp.algorithm.GeneticAlgorithm;
import genetictsp.algorithm.FitnessCalculator;

import java.io.IOException;

import genetictsp.model.Costs;
import genetictsp.util.InstanceLoader;
import genetictsp.algorithm.selection.parent.implementations.TournamentSelection;
import genetictsp.algorithm.crossover.implementations.PartiallyMappedCrossover;
import genetictsp.algorithm.mutation.implementations.SwapMutation;
import genetictsp.algorithm.selection.survivor.implementations.SteadyStateReplacement;

public class Main {
    // Archivo de instancia a resolver
    private final static String INSTANCE_FILE = "data/br17.atsp";
    
    // Parámetros generales del algoritmo genético
    private final static int POPULATION_SIZE = 200;
    private final static int MAX_GENERATIONS = 2000;

    // Parámetros específicos para los componentes
    private final static int TOURNAMENT_SIZE = 10;
    private final static int STEADY_STATE_REPLACEMENTS = 5;
    
    public static void main(String[] args) {

        // Carga de la instancia ATSP, inicialización de costos y creación del algoritmo genético
        InstanceLoader.Instance instance;
        try {
            instance = InstanceLoader.loadATSP(INSTANCE_FILE);
        } catch (IOException e) {
            System.err.println("Error loading instance file: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        Costs.getInstance(instance.costMatrix);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
            new TournamentSelection(TOURNAMENT_SIZE), 
            new PartiallyMappedCrossover(0.8), 
            new SwapMutation(0.3), 
            new SteadyStateReplacement(STEADY_STATE_REPLACEMENTS)
        );
            
        // Ejecución del algoritmo genético
        ArrayList<int[]> finalPopulation = geneticAlgorithm.run(MAX_GENERATIONS, POPULATION_SIZE, instance.dimension);

        // Impresión de resultados
        System.out.println("=== TSP Genetic Algorithm ===");
        System.out.println("Instance: " + instance.name);
        System.out.println("Dimension (cities): " + instance.dimension);
        System.out.println("Population size: " + POPULATION_SIZE);
        System.out.println("Max generations: " + MAX_GENERATIONS);
        System.out.println("Cost matrix:");
        System.out.println(Costs.getInstance().toString());
        System.out.println("\nBest solution: " + printArray(finalPopulation.get(0)));
        System.out.println("Best fitness (cost): " + FitnessCalculator.calculate(finalPopulation.get(0)));

    }

    private static String printArray(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}


