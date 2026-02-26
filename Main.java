import java.util.ArrayList;
import java.io.IOException;

import Crossover.implementations.OrderCrossover;
import General.Costs;
import General.GeneticAlgorithm;
import General.InstanceLoader;
import Mutation.implementations.SwapMutation;
import Selection.implementations.TournamentSelection;
import SurvivorSelection.implementations.SteadyStateSurvivorSelection;

public class Main {
    // Archivo de instancia a resolver
    private final static String INSTANCE_FILE = "d:/Usuario/Downloads/instancias-TSP/br17.atsp";
    
    private final static int POPULATION_SIZE = 200;
    private final static int MAX_GENERATIONS = 2000;
    private final static int MAX_CONSECUTIVE_FITNESS = 80;
    private final static int TOURNAMENT_SIZE = 10;
    
    public static void main(String[] args) {
        try {
            // Cargar instancia del archivo ATSP
            InstanceLoader.Instance instance = InstanceLoader.loadATSP(INSTANCE_FILE);
            int CITIES = instance.dimension;
            
            System.out.println("=== TSP Genetic Algorithm ===");
            System.out.println("Instance: " + instance.name);
            System.out.println("Dimension (cities): " + CITIES);
            System.out.println("Population size: " + POPULATION_SIZE);
            System.out.println("Max generations: " + MAX_GENERATIONS);
            System.out.println();
            
            // Resetear la instancia de Costs y cargar la matriz del archivo
            Costs.resetInstance();
            Costs.getInstance(instance.costMatrix);
            
            // Verificar que la matriz se cargó correctamente
            System.out.println("\n=== Cost Matrix ===");
            Costs costs = Costs.getInstance(instance.costMatrix);
            System.out.println(costs.toString());
            
            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                new TournamentSelection(TOURNAMENT_SIZE), 
                new OrderCrossover(), 
                new SwapMutation(), 
                new SteadyStateSurvivorSelection()
            );
            
            geneticAlgorithm.initializeComponents(CITIES);

            ArrayList<int[]> initialPopulation = geneticAlgorithm.initializePopulation(POPULATION_SIZE, CITIES);
            
            double[] fitness = geneticAlgorithm.evaluatePopulation(POPULATION_SIZE, initialPopulation);
            
            ArrayList<int[]> finalPopulation = geneticAlgorithm.run(MAX_GENERATIONS, MAX_CONSECUTIVE_FITNESS, initialPopulation, fitness);

            System.out.println("Best solution: " + printArray(finalPopulation.get(0)));
            System.out.println("Best fitness (cost): " + geneticAlgorithm.calculateFitness(finalPopulation.get(0)));
        } catch (IOException e) {
            System.err.println("Error loading instance file: " + e.getMessage());
            e.printStackTrace();
        }
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


