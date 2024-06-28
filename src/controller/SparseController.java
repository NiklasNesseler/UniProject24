package controller;

import model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SparseController {
    private SparseMap completeSparseMap;

    public SparseController(int[][] baseData, int[][] valueData) {
        initCompleteSparseMap(baseData, valueData);

    }

    private void initCompleteSparseMap(int[][] baseData, int[][] valueData) {
        completeSparseMap = new SparseMap(baseData);
        completeSparseMap.putValuesToBasicMap(valueData);

        for (int i = 0; i < completeSparseMap.getSparseVertexArray().length; i++) {
            for (int j = 0; j < completeSparseMap.getSparseVertexArray()[i].length; j++) {
                BasicVertex basicVertex = completeSparseMap.getSparseVertexArray()[i][j];
                if (basicVertex != null) {
                    basicVertex.setContainingMap(completeSparseMap);
                }
            }
        }
    }

    public SparseMap generateMap(int streets, int maxDeg, boolean connected) {
        if (streets < 1 || streets > 50 || maxDeg < 0 || maxDeg > 4) {
            return generateMapOfBuildings();
        }
        SparseMap sparseMap = new SparseMap(new int[10][15]);
        Random random = new Random();

        ArrayList<Position2D> streetPositions = new ArrayList<>();
        Set<Position2D> used = new HashSet<>();

        while (streetPositions.size() < streets) {
            int row = random.nextInt(10);
            int col = random.nextInt(15);
            Position2D position2D = new Position2D(row, col);

            if (!used.contains(position2D)) {
                used.add(position2D);
                streetPositions.add(position2D);
                BasicStreet street = new BasicStreet(row, col, random.nextInt(100), random.nextInt(30) + 1);
                street.setContainingMap(sparseMap);
                sparseMap.getSparseVertexArray()[row][col] = street;
            }
        }


        int streetCount = 0;
        for (BasicVertex[] row : sparseMap.getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    streetCount++;
                    int neighbourCount = 0;
                    for (BasicVertex neighbour : vertex.getNeighbours()) {
                        if (neighbour instanceof BasicStreet) {
                            neighbourCount++;
                        }
                    }

                }
            }
        }

        if (streetCount != streets) {
            return generateMapOfBuildings();
        }

        if (connected && !sparseMap.isBasicStreetConnectedMap()) {
            return generateMapOfBuildings();
        }
        return sparseMap;
    }


    public SparseMap getCompleteSparseMap() {
        return completeSparseMap;
    }

    public void setCompleteSparseMap(SparseMap completeSparseMap) {
        this.completeSparseMap = completeSparseMap;
    }

    private SparseMap generateMapOfBuildings() {
        SparseMap sparseMap = new SparseMap(new int[10][15]);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 15; j++) {
                BasicBuilding building = new BasicBuilding(i, j, 1, 1);
                building.setContainingMap(sparseMap);
                sparseMap.getSparseVertexArray()[i][j] = building;

            }
        }
        return sparseMap;
    }
}
