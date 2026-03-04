import java.util.ArrayList;
import java.io.IOException;

import Crossover.implementations.PartiallyMappedCrossover;
import General.Costs;
import General.FitnessCalculator;
import General.GeneticAlgorithm;
import General.InstanceLoader;
import Mutation.implementations.SwapMutation;
import ParentSelection.implementations.TournamentSelection;
import SurvivorSelection.implementations.SteadyStateReplacement;

public class Main {
    // Archivo de instancia a resolver
    private final static String INSTANCE_FILE = "instancias-TSP/br17.atsp";
    
    // Parámetros generales del algoritmo genético
    private final static int POPULATION_SIZE = 200;
    private final static int MAX_GENERATIONS = 2000;
    private final static int MAX_CONSECUTIVE_FITNESS = 80;

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
            new PartiallyMappedCrossover(), 
            new SwapMutation(), 
            new SteadyStateReplacement(STEADY_STATE_REPLACEMENTS)
        );
            
        // Ejecución del algoritmo genético
        ArrayList<int[]> finalPopulation = geneticAlgorithm.run(MAX_GENERATIONS, MAX_CONSECUTIVE_FITNESS, POPULATION_SIZE, instance.dimension);

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


