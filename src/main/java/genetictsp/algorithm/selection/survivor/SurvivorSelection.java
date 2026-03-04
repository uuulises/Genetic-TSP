package genetictsp.algorithm.selection.survivor;

import java.util.ArrayList;


public interface SurvivorSelection {

    ArrayList<int[]> replace(int populationLength, ArrayList<int[]> parents, ArrayList<int[]> children);

}
