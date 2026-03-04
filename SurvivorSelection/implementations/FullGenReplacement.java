package SurvivorSelection.implementations;

import java.util.ArrayList;

import SurvivorSelection.SurvivorSelection;

public class FullGenReplacement implements SurvivorSelection {
    @Override
    public ArrayList<int[]> replace(int populationLength, ArrayList<int[]> parents, ArrayList<int[]> children) {
        return new ArrayList<>(children);
    }
}