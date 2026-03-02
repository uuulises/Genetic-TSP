package Crossover.implementations;

import java.util.ArrayList;

import Crossover.Crossover;

public class PartiallyMappedCrossover implements Crossover {

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

        for (int i = pos1; i <= pos2; i++) {

            // Recorro en la sección copiada en el otro padre, buscando valores que no esten en el hijo
            if (!taken1[parent2[i]]) {
                int currentPos = i;

                // Busco que la posición final del valor encontrado caiga fuera de la sección copiada
                while (currentPos >= pos1 && currentPos <= pos2) {
                    // Mientras caiga adentro, busco otra, obteniendo la posición del valor encontrado en el otro padre
                    currentPos = findIndex(parent1, parent2[currentPos]);
                }
                child1[currentPos] = parent2[i];
                taken1[parent2[i]] = true;
            }

            if (!taken2[parent1[i]]) {
                int currentPos = i;
                while (currentPos >= pos1 && currentPos <= pos2) {
                    currentPos = findIndex(parent2, parent1[currentPos]);
                }
                child2[currentPos] = parent1[i];
                taken2[parent1[i]] = true;
            }
        }

        for (int i = 0; i < length; i++) {
            // Recorro el otro padre tomando solo los valores que no existen en el hijo
            if (!taken1[parent2[i]]) {
                // Si es un valor nuevo, lo agrego al hijo en la primera posición vacía encontrada
                for (int j = 0; j < length; j++) {
                    if (!taken1[j]) {
                        child1[j] = parent2[i];
                        taken1[j] = true;
                        break;
                    }
                }
            }

            if (!taken2[parent1[i]]) {
                for (int j = 0; j < length; j++) {
                    if (!taken2[j]) {
                        child2[j] = parent1[i];
                        taken2[j] = true;
                        break;
                    }
                }
            }
        }

        ArrayList<int[]> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        return children;
    }
    
    private int findIndex(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1; // Se asume que el valor siempre estará presente en el array, por lo que este caso no debería ocurrir
    }
}
