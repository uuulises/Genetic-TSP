package ParentSelection.implementations;
import java.util.ArrayList;

import ParentSelection.ParentSelection;

public class RouletteWheelSelection implements ParentSelection {
    @Override
    public int[] select(ArrayList<int[]> population, boolean[] selected, int populationSize, double[] fitness) {
        // Calcular fitness total
        double totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            if (!selected[i]) {
                totalFitness += fitness[i];
            }
        }

        // Manejar caso especial si todos tienen fitness 0 o negativo
        if (totalFitness <= 0) {
            // Seleccionar aleatoriamente
            int randomIndex = (int) (Math.random() * populationSize);
            selected[randomIndex] = true;
            return population.get(randomIndex);
        }

        // Generar un número aleatorio entre 0 y el fitness total
        double randomValue = Math.random() * totalFitness;
        double accumulatedFitness = 0;

        // Iterar a través de la población y encontrar el individuo seleccionado
        for (int i = 0; i < populationSize; i++) {
            if (!selected[i]) { // Saltar individuos ya seleccionados
                accumulatedFitness += fitness[i];
                if (randomValue <= accumulatedFitness) {
                    selected[i] = true;
                    return population.get(i);
                }
            }
        }

        return population.get(populationSize - 1);
    }
}
