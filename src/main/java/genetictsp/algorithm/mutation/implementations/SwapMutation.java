package genetictsp.algorithm.mutation.implementations;

import genetictsp.algorithm.mutation.Mutation;

public class SwapMutation extends Mutation {
    public SwapMutation(double probability) {
        super(probability);
    }

    @Override
    protected void performMutation(int[] individual) {
        int length = individual.length;

        int pos1 = (int) (Math.random() * length);
        int pos2 = (int) (Math.random() * length);

        while (pos1 == pos2) {
            pos2 = (int) (Math.random() * length);
        }

        int temp = individual[pos1];
        individual[pos1] = individual[pos2];
        individual[pos2] = temp;
    }
    
}
