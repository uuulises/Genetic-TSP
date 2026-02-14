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
            double currentFitness = fitness[(int) (Math.random() * populationSize)];
            if (currentFitness > bestFitness) {
                bestFitness = currentFitness;
                bestIndex = i;
            }
        }

        return population.remove(bestIndex);
        
    }
    
}
