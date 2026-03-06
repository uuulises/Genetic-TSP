package genetictsp.algorithm.mutation.implementations;

import genetictsp.algorithm.mutation.Mutation;

public class ReversalMutation extends Mutation {
    public ReversalMutation(double probability) {
        super(probability);
    }

    @Override
    protected void performMutation(int[] individual) {
        int length = individual.length;
        int index1 = (int) (Math.random() * length);
        int index2 = (int) (Math.random() * length);

        if (index1 > index2) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }

        while (index1 < index2) {

            int temp = individual[index1];
            individual[index1] = individual[index2];
            individual[index2] = temp;

            index1++;
            index2--;
            
        }
    }

    @Override
    public String getName() {
        return "Reversal";
    }
}