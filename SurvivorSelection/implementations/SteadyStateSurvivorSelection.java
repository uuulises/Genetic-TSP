package SurvivorSelection.implementations;
import java.util.ArrayList;

import java.util.function.ToDoubleFunction;

import SurvivorSelection.SurvivorSelection;

public class SteadyStateSurvivorSelection implements SurvivorSelection {
    @Override
    public ArrayList<int[]> performSelection(ArrayList<int[]> population, ArrayList<int[]> parents, ArrayList<int[]> children, ToDoubleFunction<int[]> calculateFitness) { //TODO sacar ga de acá
        int originalLength = population.size();

        population = new ArrayList<>(parents);
        population.addAll(children);

        population.sort((a, b) -> {
            double fitnessA = calculateFitness.applyAsDouble(a);
            double fitnessB = calculateFitness.applyAsDouble(b);
            return Double.compare(fitnessB, fitnessA); 
        });

        while (population.size() > originalLength) {
            population.remove(population.size() - 1);
        }

        return population;
    }
}