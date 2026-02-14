package SurvivorSelection;

import java.util.ArrayList;
import java.util.function.ToDoubleFunction;


public interface SurvivorSelection {

    ArrayList<int[]> performSelection(ArrayList<int[]> population, ArrayList<int[]> parents, ArrayList<int[]> children, ToDoubleFunction<int[]> calculateFitness);

}
