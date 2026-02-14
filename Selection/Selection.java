package Selection;
import java.util.ArrayList;

public interface Selection {
    public abstract int[] select(ArrayList<int[]> population, int populationSize, double[] fitness);
}
