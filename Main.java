import java.util.ArrayList;

import Crossover.implementations.OrderCrossover;
import General.GeneticAlgorithm;
import Mutation.implementations.SwapMutation;
import Selection.implementations.TournamentSelection;
import SurvivorSelection.implementations.SteadyStateSurvivorSelection;

public class Main {
    private final static int CITIES = 8; // IMPORTANT: PERMUTATIONS(CITIES,CITIES) >= POPULATION_SIZE
    private final static int POPULATION_SIZE = 200;
    private final static int MAX_GENERATIONS = 2000;
    private final static int MAX_CONSECUTIVE_FITNESS = 80;
    private final static int TOURNAMENT_SIZE = 10;
    public static void main(String[] args) {

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
            new TournamentSelection(TOURNAMENT_SIZE), 
            new OrderCrossover(), 
            new SwapMutation(), 
            new SteadyStateSurvivorSelection()
        );
        
        geneticAlgorithm.initializeComponents(CITIES);

        ArrayList<int[]> population = geneticAlgorithm.initializePopulation(POPULATION_SIZE, CITIES);
        
        double[] fitness = geneticAlgorithm.evaluatePopulation(POPULATION_SIZE, population);
        
        geneticAlgorithm.run(MAX_GENERATIONS, MAX_CONSECUTIVE_FITNESS, population, fitness);

        System.out.println("Best fitness: " + printArray(population.get(0)) + " " + geneticAlgorithm.calculateFitness(population.get(0)));
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


