package Mutation.implementations;

import Mutation.Mutation;

public class ReversalMutation implements Mutation {
    @Override
    public void mutate(int[] individual) {
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
}