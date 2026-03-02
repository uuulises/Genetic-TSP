package SurvivorSelection.implementations;

import java.util.ArrayList;
import java.util.function.ToDoubleFunction;

import SurvivorSelection.SurvivorSelection;

public class FullGenReplacement implements SurvivorSelection {
    @Override
    public ArrayList<int[]> performSelection(ArrayList<int[]> population, ArrayList<int[]> parents, ArrayList<int[]> children, ToDoubleFunction<int[]> calculateFitness) {
        return new ArrayList<>(children);
    }
}