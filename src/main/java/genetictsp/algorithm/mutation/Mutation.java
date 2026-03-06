package genetictsp.algorithm.mutation;

public abstract class Mutation {
    private double probability;

    public Mutation(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    public void mutate(int[] individual) {
        if (Math.random() < probability) {
            performMutation(individual);
        }
    }

    protected abstract void performMutation(int[] individual);

    public abstract String getName();
}
