package genetictsp.algorithm.selection.survivor;

import java.util.ArrayList;


public interface SurvivorSelection {

    public ArrayList<int[]> replace(int populationLength, ArrayList<int[]> parents, ArrayList<int[]> children);

    public String getName();

}
