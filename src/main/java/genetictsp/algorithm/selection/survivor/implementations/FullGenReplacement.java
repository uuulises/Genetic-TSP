package genetictsp.algorithm.selection.survivor.implementations;

import java.util.ArrayList;

import genetictsp.algorithm.selection.survivor.SurvivorSelection;

public class FullGenReplacement implements SurvivorSelection {
    @Override
    public ArrayList<int[]> replace(int populationLength, ArrayList<int[]> parents, ArrayList<int[]> children) {
        return new ArrayList<>(children);
    }
}