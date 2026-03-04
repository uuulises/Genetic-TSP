package genetictsp.algorithm;

import java.util.ArrayList;

public class PopulationManager {

    public static ArrayList<int[]> initializePopulation(int populationSize, int cities) {
        ArrayList<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            int[] tour = generateRandomTour(cities);
            while (containsDuplicate(population, tour, i)) {
                tour = generateRandomTour(cities);
            }
            population.add(tour);
        }

        return population;
    }

    public static double[] evaluatePopulation(int populationSize, ArrayList<int[]> population) {
        double[] fitness = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            fitness[i] = FitnessCalculator.calculate(population.get(i));
        }
        return fitness;
    }

    private static boolean containsDuplicate(ArrayList<int[]> population, int[] tour, int i) {
        for (int j = 0; j < i; j++) {
            boolean isDuplicate = true;
            for (int k = 0; k < tour.length; k++) {
                if (population.get(j)[k] != tour[k]) {
                    isDuplicate = false;
                    break;
                }
            }
            if (isDuplicate) {
                return true;
            }
        }
        return false;
    }

    private static int[] generateRandomTour(int cities) {
        int[] tour = new int[cities];
        for (int i = 0; i < cities; i++) {
            tour[i] = i;
        }
        
        for (int i = 0; i < cities; i++) {
            int j = (int) (Math.random() * cities);
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
        }

        return tour;
    }
}
