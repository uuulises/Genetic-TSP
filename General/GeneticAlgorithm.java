package General;
import java.util.ArrayList;

import Crossover.Crossover;
import Mutation.Mutation;
import ParentSelection.ParentSelection;
import SurvivorSelection.SurvivorSelection;

public class GeneticAlgorithm {
    private ParentSelection parentSelection;
    private Crossover crossover;
    private Mutation mutation;
    private SurvivorSelection survivorSelection;

    public GeneticAlgorithm(ParentSelection parentSelection, Crossover crossover, Mutation mutation, SurvivorSelection survivorSelection) {
        this.parentSelection = parentSelection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.survivorSelection = survivorSelection;
    }

    public ArrayList<int[]> run(int maxGenerations, int maxConsecutiveFitness, int populationSize, int cities) {

        ArrayList<int[]> population = PopulationManager.initializePopulation(populationSize, cities);
        double[] fitness = PopulationManager.evaluatePopulation(populationSize, population);

        int generation = 0;
        int consecuentiveFitness = 0;
        double bestFitness = 0;
        while (generation < maxGenerations && consecuentiveFitness < maxConsecutiveFitness) {
            generation++;

            ArrayList<int[]> parents = new ArrayList<>(population);
            boolean[] selected = new boolean[population.size()];
            ArrayList<int[]> children = new ArrayList<>();
            while (parents.size() - children.size() > 1) {
                int[] parent1 = parentSelection.select(parents, selected, parents.size(), fitness);
                int[] parent2 = parentSelection.select(parents, selected, parents.size(), fitness); 
                
                children.addAll(crossover.crossover(parent1, parent2));
            }

            for (int[] child : children) {
                mutation.mutate(child);
            }
            
            population = survivorSelection.replace(population.size(), population, children);

            double currentBestFitness = FitnessCalculator.calculate(population.get(0));

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
