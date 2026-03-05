package genetictsp.algorithm.crossover;
import java.util.ArrayList;

public abstract class Crossover {
    private double probability;

    public Crossover(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    public ArrayList<int[]> crossover(int[] parent1, int[] parent2){
        if (Math.random() < probability) {
            return performCrossover(parent1, parent2);
        } else {
            ArrayList<int[]> children = new ArrayList<>();
            children.add(parent1.clone());
            children.add(parent2.clone());
            return children;
        }
    }

    protected abstract ArrayList<int[]> performCrossover(int[] parent1, int[] parent2);
}
