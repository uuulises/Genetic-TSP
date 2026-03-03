package Selection.implementations;
import java.util.ArrayList;

import Selection.Selection;

public class RouletteWheelSelection implements Selection {
    @Override
    public int[] select(ArrayList<int[]> population, int populationSize, double[] fitness) {
        // Calcular fitness total
        double totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            totalFitness += fitness[i];
        }

        // Manejar caso especial si todos tienen fitness 0 o negativo
        if (totalFitness <= 0) {
            // Seleccionar aleatoriamente
            int randomIndex = (int) (Math.random() * populationSize);
            return population.remove(randomIndex);
        }

        // Generar un número aleatorio entre 0 y el fitness total
        double randomValue = Math.random() * totalFitness;
        double accumulatedFitness = 0;

        // Iterar a través de la población y encontrar el individuo seleccionado
        for (int i = 0; i < populationSize; i++) {
            accumulatedFitness += fitness[i];
            if (randomValue <= accumulatedFitness) {
                return population.remove(i);
            }
        }

        return population.remove(populationSize - 1);
    }
}
