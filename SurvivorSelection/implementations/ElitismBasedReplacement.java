package SurvivorSelection.implementations;
import java.util.ArrayList;

import General.FitnessCalculator;
import SurvivorSelection.SurvivorSelection;

public class ElitismBasedReplacement implements SurvivorSelection {
    @Override
    public ArrayList<int[]> replace(int populationLength, ArrayList<int[]> parents, ArrayList<int[]> children) {
        ArrayList<int[]> population = new ArrayList<>(parents);
        population.addAll(children);

        population.sort((a, b) -> {
            double fitnessA = FitnessCalculator.calculate(a);
            double fitnessB = FitnessCalculator.calculate(b);
            return Double.compare(fitnessB, fitnessA); 
        });

        while (population.size() > populationLength) {
            population.remove(population.size() - 1);
        }

        return population;
    }
}