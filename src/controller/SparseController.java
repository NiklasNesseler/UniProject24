package controller;

import model.*;

import java.util.*;

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
        List<Position2D> positions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 15; j++) {

                positions.add(new Position2D(i, j));

            }
        }

        Collections.shuffle(positions, random);
        List<BasicStreet> streetsList = new ArrayList<>();
        for (int i = 0; i < streets; i++) {
            Position2D position = positions.get(i);
            BasicStreet street = new BasicStreet(position.getRow(), position.getColumn(), random.nextInt(100), random.nextInt(30) + 1);
            street.setContainingMap(sparseMap);
            sparseMap.getSparseVertexArray()[position.getRow()][position.getColumn()] = street;
            streetsList.add(street);
        }

        for (int i = streets; i < positions.size(); i++) {
            Position2D position = positions.get(i);
            BasicGreen green = new BasicGreen(position.getRow(), position.getColumn(), random.nextInt(100));
            green.setContainingMap(sparseMap);
            sparseMap.getSparseVertexArray()[position.getRow()][position.getColumn()] = green;
        }



        if (!meetsCase1Reqs(streetsList, maxDeg, connected, sparseMap)) {
            return generateMapOfBuildings();
        }

        return sparseMap;
    }

    private boolean meetsCase1Reqs(List<BasicStreet> streetsList, int maxDeg, boolean connected, SparseMap map) {
        if (connected && !map.isBasicStreetConnectedMap()) {
            return false;
        }

        for (BasicStreet street : streetsList) {
            if (street.getNeighbours().size() > maxDeg) {
                return false;
            }
        }

        return true;
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
