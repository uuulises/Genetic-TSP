package Selection.implementations;
import java.util.ArrayList;

import Selection.Selection;

public class TournamentSelection implements Selection {
    private int tournamentSize;

    public TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public int[] select(ArrayList<int[]> population, int populationSize, double[] fitness) {
        int bestIndex = -1;
        double bestFitness = Double.NEGATIVE_INFINITY;

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

        return population.remove(bestIndex);
        
    }
    
}
