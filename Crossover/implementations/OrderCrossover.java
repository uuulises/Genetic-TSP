package Crossover.implementations;
import java.util.ArrayList;

import Crossover.Crossover;

public class OrderCrossover implements Crossover {
    @Override
    public ArrayList<int[]> crossover(int[] parent1, int[] parent2) {
        int length = parent1.length;

        int[] child1 = new int[length];
        boolean[] taken1 = new boolean[length];
        int[] child2 = new int[length];
        boolean[] taken2 = new boolean[length];

        int pos1 = (int) (Math.random() * length);
        int pos2 = (int) (Math.random() * length);

        if (pos1 > pos2) {
            int temp = pos1;
            pos1 = pos2;
            pos2 = temp;
        }
    
        for (int i = pos1; i <= pos2; i++) {
            child1[i] = parent1[i];
            taken1[child1[i]] = true;

            child2[i] = parent2[i];
            taken2[child2[i]] = true;
        }

        int currentPos1 = (pos2 + 1) % length;
        int currentPos2 = (pos2 + 1) % length;

        for (int i = (pos2 + 1) % length; i != pos1; i = (i + 1) % length) {
            while(taken1[parent2[currentPos1]]) {
                currentPos1 = (currentPos1 + 1) % length;
            }
            child1[i] = parent2[currentPos1];
            taken1[child1[i]] = true;
            currentPos1 = (currentPos1 + 1) % length;

            while(taken2[parent1[currentPos2]]) {
                currentPos2 = (currentPos2 + 1) % length;
            }
            child2[i] = parent1[currentPos2];
            taken2[child2[i]] = true;
            currentPos2 = (currentPos2 + 1) % length;
        }

        ArrayList<int[]> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        return children;
    }
}
