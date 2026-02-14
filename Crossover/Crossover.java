package Crossover;
import java.util.ArrayList;

public interface Crossover {
    public abstract ArrayList<int[]> crossover(int[] parent1, int[] parent2);
}
