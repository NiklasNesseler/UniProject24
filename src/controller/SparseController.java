package controller;

import model.*;

import java.util.*;


/**
 * Class to initialize and manage a SparseMap
 */
public class SparseController {
    /**
     * Represents the full sparse map
     */
    private SparseMap completeSparseMap;


    /**
     * Constructor for the SparseControlle
     * @param baseData represents the base data for the map
     * @param valueData represents the value data for the map
     */
    public SparseController(int[][] baseData, int[][] valueData) {
        initCompleteSparseMap(baseData, valueData);

    }

    /**
     * Initializes the completeSparseMap
     * @param baseData represents the base data for the map
     * @param valueData represents the value data for the map
     */
    public void initCompleteSparseMap(int[][] baseData, int[][] valueData) {
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

    /**
     * Generates the SParseMap based on the required Parameters
     * @param streets number of street vertices in the map
     * @param maxDeg maximum degree of street vertices
     * @param connected if the streets should be connected
     * @return the generated Map
     */
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

    /**
     * Checks for the Requirements of Case 1
     * @param streetsList list of street vertices in the map
     * @param maxDeg maximum degree of street nodes
     * @param connected if the street nodes should be connected
     * @param map the sparse map object
     * @return if case 1 is the case, false otherwise
     */
    private boolean meetsCase1Reqs(List<BasicStreet> streetsList, int maxDeg, boolean connected, SparseMap map) {
        if (connected && !map.isBasicStreetConnectedMap()) {
            return false;
        }

        for (BasicStreet street : streetsList) {
            int streetNeighbourCount = 0;
            for (BasicVertex neighbour : street.getNeighbours()) {
                if (neighbour instanceof BasicStreet) {
                    streetNeighbourCount++;
            }}
            if (streetNeighbourCount > maxDeg) {
                return false;
            }


        }

        return true;
    }


    /**
     *
     * @return the complete sparse map
     */
    public SparseMap getCompleteSparseMap() {
        return completeSparseMap;
    }

    /**
     * Sets the complete sparse map
     * @param completeSparseMap sparse map object to be set
     */
    public void setCompleteSparseMap(SparseMap completeSparseMap) {
        this.completeSparseMap = completeSparseMap;
    }

    /**
     * Generates the Map of Buildings if case 2 is met
     * @return the generated Map of Buildings
     */
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
