package genetictsp.algorithm.selection.parent;

import java.util.ArrayList;

public interface ParentSelection {
    public abstract int[] select(ArrayList<int[]> population, boolean[] selected, int populationSize, double[] fitness);
    public String getName();
}
