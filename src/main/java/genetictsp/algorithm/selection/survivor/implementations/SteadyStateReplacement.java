package genetictsp.algorithm.selection.survivor.implementations;

import java.util.ArrayList;

import genetictsp.algorithm.selection.survivor.SurvivorSelection;
import genetictsp.algorithm.FitnessCalculator;

public class SteadyStateReplacement implements SurvivorSelection {
    private int numReplacements;

    public SteadyStateReplacement(int numReplacements) {
        this.numReplacements = numReplacements;
    }

    @Override
    public ArrayList<int[]> replace(int populationLength, ArrayList<int[]> parents, ArrayList<int[]> children) {

        ArrayList<int[]> population = new ArrayList<>(parents);

        // Ordenar población por fitness (de peor a mejor)
        population.sort((a, b) -> {
            double fitnessA = FitnessCalculator.calculate(a);
            double fitnessB = FitnessCalculator.calculate(b);
            return Double.compare(fitnessA, fitnessB); 
        });

        // Ordenar hijos por fitness (de mejor a peor)
        children.sort((a, b) -> {
            double fitnessA = FitnessCalculator.calculate(a);
            double fitnessB = FitnessCalculator.calculate(b);
            return Double.compare(fitnessB, fitnessA); 
        });

        // Remplazo los n peores individuos de la población con los n mejores hijos
        for (int i = 0; i < numReplacements; i++) {
            if (i < children.size() && i < population.size()) { // Condición de seguridad necesaria para asegurarse de no exceder los límites de los arrays
                population.set(population.size() - 1 - i, children.get(i));
            }
        }

        return population;
    }
    
}
