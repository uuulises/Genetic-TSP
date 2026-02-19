package General;
import java.util.ArrayList;

import Crossover.Crossover;
import Mutation.Mutation;
import Selection.Selection;
import SurvivorSelection.SurvivorSelection;

public class GeneticAlgorithm {
    private Selection parentSelection;
    private Crossover crossover;
    private Mutation mutation;
    private SurvivorSelection survivorSelection;

    private static Costs costs;

    private static boolean debug = false;

    public GeneticAlgorithm(Selection parentSelection, Crossover crossover, Mutation mutation, SurvivorSelection survivorSelection) {
        this.parentSelection = parentSelection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.survivorSelection = survivorSelection;
    }

    public void initializeComponents(int cities) {
        costs = Costs.getInstance(cities);

        if (debug) {
            System.out.println("Cost Matrix:");
            System.out.println(costs.toString());
        }
    }

    public ArrayList<int[]> initializePopulation(int populationSize, int cities) {
        ArrayList<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            if (debug) {
                System.out.println(i + "\n");
            }

            int[] tour = generateRandomTour(cities);
            while (containsDuplicate(population, tour, i)) {
                tour = generateRandomTour(cities);
            }
            population.add(tour);
        }

        return population;
    }

    public double[] evaluatePopulation(int populationSize, ArrayList<int[]> population) {
        double[] fitness = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            fitness[i] = calculateFitness(population.get(i));
        }
        return fitness;
    }

    private static boolean containsDuplicate(ArrayList<int[]> population, int[] tour, int i) {
        for (int j = 0; j < i; j++) {
            boolean isDuplicate = true;
            for (int k = 0; k < tour.length; k++) {
                if (population.get(j)[k] != tour[k]) {
                    isDuplicate = false;
                    break;
                }
            }
            if (isDuplicate) {
                return true;
            }
        }
        return false;
    }

    private static int[] generateRandomTour(int cities) {
        int[] tour = new int[cities];
        for (int i = 0; i < cities; i++) {
            tour[i] = i;
        }
        
        for (int i = 0; i < cities; i++) {
            int j = (int) (Math.random() * cities);
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
        }

        if (debug) {
            for (int city : tour) {
                System.out.print(city + " ");
            }
            System.out.println();
        }

        return tour;
    }

    public double calculateFitness(int[] path) {
        int totalCost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            totalCost += costs.getCost(path[i], path[i + 1]);
        }
        totalCost += costs.getCost(path[path.length - 1], path[0]);
        return 100.0 / totalCost;
    }

    public ArrayList<int[]> run(int maxGenerations, int maxConsecutiveFitness, ArrayList<int[]> initialPopulation, double[] fitness) {

        ArrayList<int[]> population = new ArrayList<>(initialPopulation);

        int generation = 0;
        int consecuentiveFitness = 0;
        double bestFitness = 0;
        while (generation < maxGenerations && consecuentiveFitness < maxConsecutiveFitness) {
            generation++;

            ArrayList<int[]> parents = new ArrayList<>(population);
            ArrayList<int[]> children = new ArrayList<>();
            while (parents.size() > 1) {
                int[] parent1 = parentSelection.select(parents, parents.size(), fitness);
                int[] parent2 = parentSelection.select(parents, parents.size(), fitness); 
                
                children.addAll(crossover.crossover(parent1, parent2));
            }

            for (int[] child : children) {
                mutation.mutate(child);
            }
            
            population = survivorSelection.performSelection(population, parents, children, this::calculateFitness);

            double currentBestFitness = calculateFitness(population.get(0));

            if (currentBestFitness > bestFitness) {
                bestFitness = currentBestFitness;
                consecuentiveFitness = 0;
            } else {
                consecuentiveFitness++;
            }

        }
        return population;
    }

}
