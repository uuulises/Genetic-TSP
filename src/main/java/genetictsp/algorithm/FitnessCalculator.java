package genetictsp.algorithm;

import genetictsp.model.Costs;

public class FitnessCalculator {
    public static double calculate(int[] tour) {

        Costs costs = Costs.getInstance();
        double totalCost = 0;

        for (int i = 0; i < tour.length - 1; i++) {
            totalCost += costs.getCost(tour[i], tour[i + 1]);
        }
        totalCost += costs.getCost(tour[tour.length - 1], tour[0]);
        return 100.0 / totalCost;
    } 
}
