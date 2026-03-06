package genetictsp.algorithm.crossover.implementations;

import java.util.ArrayList;
import java.util.Arrays;

import genetictsp.algorithm.crossover.Crossover;

public class PartiallyMappedCrossover extends Crossover {

    public PartiallyMappedCrossover(double probability) {
        super(probability);
    }

    @Override
    protected ArrayList<int[]> performCrossover(int[] parent1, int[] parent2) {
        int length = parent1.length;

        int[] child1 = new int[length];
        int[] child2 = new int[length];
        
        // IMPORTANTE: Inicializar con -1, porque 0 es una ciudad válida
        Arrays.fill(child1, -1);
        Arrays.fill(child2, -1);

        int pos1 = (int) (Math.random() * length);
        int pos2 = (int) (Math.random() * length);

        if (pos1 > pos2) {
            int temp = pos1;
            pos1 = pos2;
            pos2 = temp;
        }
    
        // Copia del segmento
        for (int i = pos1; i <= pos2; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
        }

        // Mapeo de valores
        for (int i = pos1; i <= pos2; i++) {
            // Para hijo 1
            if (!contains(child1, pos1, pos2, parent2[i])) {
                int currentPos = findIndex(parent2, parent1[i]);
                while (currentPos >= pos1 && currentPos <= pos2) {
                    currentPos = findIndex(parent2, parent1[currentPos]);
                }
                child1[currentPos] = parent2[i];
            }

            // Para hijo 2
            if (!contains(child2, pos1, pos2, parent1[i])) {
                int currentPos = findIndex(parent1, parent2[i]);
                while (currentPos >= pos1 && currentPos <= pos2) {
                    currentPos = findIndex(parent1, parent2[currentPos]);
                }
                child2[currentPos] = parent1[i];
            }
        }

        // Rellenar lo que falta
        for (int i = 0; i < length; i++) {
            if (child1[i] == -1) child1[i] = parent2[i];
            if (child2[i] == -1) child2[i] = parent1[i];
        }

        ArrayList<int[]> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        return children;
    }

    // Método auxiliar para evitar el error de "ya existe en el segmento"
    private boolean contains(int[] child, int start, int end, int value) {
        for (int i = start; i <= end; i++) {
            if (child[i] == value) return true;
        }
        return false;
    }
    
    private int findIndex(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return i;
        }
        return -1;
    }

    @Override
    public String getName() {
        return "PMX";
    }
}