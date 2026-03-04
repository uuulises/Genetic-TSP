package General;
public final class Costs {

    private static Costs instance;
    public int[][] costMatrix;

    // Creación de la matriz de costos aleatoria
    private Costs(int size) {
        costMatrix = new int[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                costMatrix[i][j] = (int) (Math.random() * 100);
            }
        }
        
    }
    public static Costs getInstance(int size) {
        if (instance == null) {
            instance = new Costs(size);
        }
        return instance;
    }

    // Creación de la matriz de costos a partir de una matriz dada
    private Costs(int[][] matrix) {
        this.costMatrix = matrix;
    }
    public static Costs getInstance(int[][] matrix) {
        if (instance == null) {
            instance = new Costs(matrix);
        }
        return instance;
    }

    // Obtención de la matriz de costos, si existe
    public static Costs getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Costs instance not initialized. Call getInstance with parameters first.");
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public int getCost(int from, int to) {
        return costMatrix[from][to];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : costMatrix) {
            for (int cost : row) {
                sb.append(String.format("%4d", cost));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}