package genetictsp.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InstanceLoader {
    public static class Instance {
        public int dimension;
        public int[][] costMatrix;
        public String name;

        public Instance(int dimension, int[][] costMatrix, String name) {
            this.dimension = dimension;
            this.costMatrix = costMatrix;
            this.name = name;
        }
    }

    public static Instance loadATSP(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String name = "";
            int dimension = 0;
            int[][] costMatrix = null;
            boolean readingWeights = false;

            String line;
            int rowIndex = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("COMMENT")) {
                    continue;
                }

                if (line.startsWith("NAME:")) {
                    name = line.substring(5).trim();
                }

                if (line.startsWith("DIMENSION:")) {
                    dimension = Integer.parseInt(line.split(":")[1].trim());
                    costMatrix = new int[dimension][dimension];
                }

                if (line.equals("EDGE_WEIGHT_SECTION")) {
                    readingWeights = true;
                    continue;
                }

                if (line.equals("EOF")) {
                    break;
                }

                if (readingWeights && !line.isEmpty()) {
                    String[] values = line.trim().split("\\s+");
                    for (int colIndex = 0; colIndex < values.length; colIndex++) {
                        costMatrix[rowIndex][colIndex] = Integer.parseInt(values[colIndex]);
                    }
                    rowIndex++;
                }
            }

            return new Instance(dimension, costMatrix, name);
        }
    }
}
