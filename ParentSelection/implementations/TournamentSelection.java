package ParentSelection.implementations;
import java.util.ArrayList;

import ParentSelection.ParentSelection;

public class TournamentSelection implements ParentSelection {
    private int tournamentSize;

    public TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public int[] select(ArrayList<int[]> population, boolean[] selected, int populationSize, double[] fitness) {
        int bestIndex = -1;
        double bestFitness = Double.NEGATIVE_INFINITY;

        populationSize = countNotSelected(population,selected);

        if(populationSize < tournamentSize) {
            tournamentSize = populationSize;
        }

        for (int i = 0; i < tournamentSize; i++) { //TODO no se controla si se repiten individuos, no está necesariamente mal, es una desición
            int currentIndex = (int) (Math.random() * populationSize);
            double currentFitness = fitness[currentIndex];
            if (currentFitness > bestFitness) {
                bestFitness = currentFitness;
                bestIndex = currentIndex;
            }
        }

        selected[bestIndex] = true;
        return population.get(bestIndex);
        
    }

    private int countNotSelected(ArrayList<int[]> population, boolean[] selected) {
        int count = 0;
        for (int i = 0; i < population.size(); i++) {
            if (!selected[i]) {
                count++;
            }
        }
        return count;
    }
    
}
