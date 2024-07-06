package model.advanced;

import model.*;

import java.util.*;


/**
 * Represents an advanced version of a basic map
 */
public class AdvancedMap{
    /**
     * 2D array of advanced vertices representing the advanced map
     */
    private AdvancedVertex[][] advancedVertexArray;

    /**
     * Constructor of the AdvancedMap from the BasicMap
     * @param basicMap used to construct the advanced map
     */
    public AdvancedMap(BasicMap basicMap) {
        initRefinement(basicMap);
    }

    /**
     * Initializes the advancedVertexArray
     * @param basicMap object
     */
    void initRefinement(BasicMap basicMap) {
        BasicVertex[][] vertexArray = basicMap.getVertexArray();
        int rows = vertexArray.length;
        int columns = vertexArray[0].length;

        advancedVertexArray = new AdvancedVertex[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                BasicVertex basicVertex = vertexArray[i][j];
                switch (basicVertex) {
                    case BasicStreet street ->
                            advancedVertexArray[i][j] = new AdvancedStreet(i, j, basicVertex.getValue(), AdvancedStreet.StreetTypes.DUMMY);
                    case BasicGreen basicGreen ->
                            advancedVertexArray[i][j] = new AdvancedGreen(i, j, basicVertex.getValue());
                    case BasicBuilding building ->
                            advancedVertexArray[i][j] = new AdvancedBuilding(i, j, basicVertex.getValue());
                    case null, default -> {
                        assert basicVertex != null;
                        advancedVertexArray[i][j] = new AdvancedVertex(i, j, basicVertex.getValue());
                    }
                }

                if (advancedVertexArray[i][j] != null) {
                    advancedVertexArray[i][j].setContainingAdvancedMap(this);
                }
            }
        }
    }


    /**
     * builds the advanced streets in the advanced map using the specific street types
     * @param streetCodes differentiating the different street types
     */
    public void buildAdvancedStreets(int[][] streetCodes) {
        if (streetCodes == null || streetCodes.length != advancedVertexArray.length
                || streetCodes[0].length != advancedVertexArray[0].length) {
            throw new IllegalArgumentException("Invalid streetCodes dimensions");
        }
        for (int i = 0; i < streetCodes.length; i++) {
            for (int j = 0; j < streetCodes[i].length; j++) {
                AdvancedVertex vertex = advancedVertexArray[i][j];
                if (vertex instanceof AdvancedStreet street && ((AdvancedStreet) vertex).getType() == AdvancedStreet.StreetTypes.DUMMY) {
                    int code = streetCodes[i][j];
                    int x = code / 10;
                    int y = code % 10;


                    AdvancedStreet.StreetTypes newStreetType = switch (x) {
                        case 1 -> AdvancedStreet.StreetTypes.CROSSING;
                        case 2 -> AdvancedStreet.StreetTypes.TJUNCTION;
                        case 3 -> AdvancedStreet.StreetTypes.LINE;
                        case 4 -> AdvancedStreet.StreetTypes.CURVE;
                        case 5 -> AdvancedStreet.StreetTypes.NEEDLE;
                        default -> throw new IllegalArgumentException("Unknown street type: " + x);
                    };

                    street.setType(newStreetType);
                    street.initParts(newStreetType);

                    for (int k = 0; k < y; k++) {
                        street.rotate90();
                    }
                }
            }
        }
    }



    /**
     * Checks if a list of streets forms an advanced path
     * @param streetList list of streets possibly representing a path
     * @return true if list is an advanced path, false otherwise
     */
    public boolean isAdvancedPath(ArrayList<AdvancedStreet> streetList) {
        if (streetList == null || streetList.isEmpty()) {
            return false;
        }

        AdvancedStreet first = streetList.getFirst();

        Set<AdvancedStreet> visited = new HashSet<>();
        visited.add(first);

        for (int i = 0; i < streetList.size() - 1; i++) {
            AdvancedStreet current = streetList.get(i);
            AdvancedStreet next = streetList.get(i + 1);
            if (visited.contains(next)) {
                return false;
            }


            if (current.getType().equals(AdvancedStreet.StreetTypes.CROSSING) || current.getType().equals(AdvancedStreet.StreetTypes.TJUNCTION)
            || current.getType().equals(AdvancedStreet.StreetTypes.DUMMY)) {
                return false;
            }

            if (!areStreetsConnected(current, next)) {
                return false;
            }

        }
        return true;
    }

    /**
     * Helper Method checking if two advanced streets are connected
     * @param current the current street
     * @param next the next street
     * @return true if the streets are connected, false otherwise
     */
    private boolean areStreetsConnected(AdvancedStreet current, AdvancedStreet next) {
        int r1 = current.getPosition().getRow();
        int c1 = current.getPosition().getColumn();
        int r2 = next.getPosition().getRow();
        int c2 = next.getPosition().getColumn();

        if (r1 == r2 && c1 == c2 + 1) {
            return current.getParts()[1][0] && next.getParts()[1][2];
        }
        if (r1 == r2 && c1 == c2 - 1) {
            return current.getParts()[1][2] && next.getParts()[1][0];
        }
        if (r1 == r2 + 1 && c1 == c2) {
            return current.getParts()[0][1] && next.getParts()[2][1];
        }
        if (r1 == r2 - 1 && c1 == c2) {
            return current.getParts()[2][1] && next.getParts()[0][1];
        }
        return false;
    }


    /**
     * Checks if a list of advanced streets forms an advanced circle
     * @param streetList list of streets, possibly representing a circle
     * @return
     */
    public boolean isAdvancedCircle(ArrayList<AdvancedStreet> streetList) {
        if (streetList == null || streetList.isEmpty() || streetList.size() < 4) {
            return false;
        }
        AdvancedStreet first = streetList.getFirst();
        AdvancedStreet last = streetList.getLast();

        if (!first.equals(last)) {
            return false;
        }

        ArrayList<AdvancedStreet> path = new ArrayList<>(streetList.subList(0, streetList.size() - 1));
        if (!isAdvancedPath(path)) {
            return false;
        }

        AdvancedStreet secondToLast = streetList.get(streetList.size() - 2);
        Set<AdvancedStreet> visited = new HashSet<>();

        return secondToLast.isAdvancedStreetConnectedTo(last, visited);
    }

    /**
     * Count the number of advanced circles in the advanced map
     * @return the number of advanced circles
     */
    public int countAdvancedCircles() {
        Set<AdvancedStreet> visited = new HashSet<>();
        int circleCount = 0;

        for (AdvancedVertex[] row : advancedVertexArray) {
            for (AdvancedVertex vertex : row) {
                if (vertex instanceof AdvancedStreet street && !visited.contains(street)) {
                    ArrayList<AdvancedStreet> path = new ArrayList<>();
                    if (dfsCircle(street, street, path, visited)) {
                        circleCount++;
                    }
                }
            }
        }

        return circleCount;
    }

    /**
     * depth first search to find advanced circles in the advanced map
     * @param start starting street vertex
     * @param current current street vertex
     * @param path current path
     * @param visited set of visited streets
     * @return true if a circle is found, false otherwise
     */
    private boolean dfsCircle(AdvancedStreet start, AdvancedStreet current, ArrayList<AdvancedStreet> path, Set<AdvancedStreet> visited) {
        visited.add(current);
        path.add(current);

        for (AdvancedStreet neighbor : getAdvancedStreetNeighborsAsList(current)) {
            if (!visited.contains(neighbor)) {
                if (dfsCircle(start, neighbor, path, visited)) {
                    return true;
                }
            } else if (neighbor.equals(start) && path.size() > 2) {
                path.add(start);
                return isAdvancedCircle(path);
            }
        }

        path.removeLast();
        return false;
    }

    /**
     * Gets a list of neighbouring streets connected to a given street
     * @param street given streets
     * @return a list of connected neighbouring streets
     */
    private List<AdvancedStreet> getAdvancedStreetNeighborsAsList(AdvancedStreet street) {
        List<AdvancedStreet> neighbors = new ArrayList<>();
        int row = street.getPosition().getRow();
        int col = street.getPosition().getColumn();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValidPosition(newRow, newCol) && advancedVertexArray[newRow][newCol] instanceof AdvancedStreet neighbor) {
                if (areStreetsConnected(street, neighbor)) {
                    neighbors.add(neighbor);
                }
            }
        }

        return neighbors;
    }

    /**
     * Helper method finding out if the given position is valid in the vertex array
     * @param row row index
     * @param col column index
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < advancedVertexArray.length && col >= 0 && col < advancedVertexArray[0].length;
    }


    /**
     * Checks if advanced map has a skeleton
     * @return true if the map has a skeleton, false otherwise
     */
    public boolean hasSkeleton() {
        Set<AdvancedStreet> streets = new HashSet<>();

        // Collect all AdvancedStreet objects in advancedVertexArray
        for (AdvancedVertex[] advancedVertices : advancedVertexArray) {
            for (AdvancedVertex advancedVertex : advancedVertices) {
                if (advancedVertex instanceof AdvancedStreet) {
                    streets.add((AdvancedStreet) advancedVertex);
                }
            }
        }

        if (streets.size() < 2) {
            return false;
        }

        for (AdvancedStreet street : streets) {
            if (street.getType().equals(AdvancedStreet.StreetTypes.DUMMY))

                return false;
        }

        for (AdvancedStreet street : streets) {
            boolean neighbourFound = false;
            for (AdvancedStreet neighbour : getAdvancedStreetNeighbours(street)) {
                if (areStreetsConnected(street, neighbour)) {
                    neighbourFound = true;
                } else {

                    return false;
                }
            }
            if (!neighbourFound) {
                return false;
            }

            if (!skeletonReq4(street)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Gets the set of neighbouring streets that are connected to a given street
     * @param street where we want to find the neighbours
     * @return a set of connected neighbours
     */
    private Set<AdvancedStreet> getAdvancedStreetNeighbours(AdvancedStreet street) {
        Set<AdvancedStreet> neighbours = new HashSet<>();
        int row = street.getPosition().getRow();
        int column = street.getPosition().getColumn();

        if (row > 0 && advancedVertexArray[row - 1][column] instanceof AdvancedStreet) {
            neighbours.add((AdvancedStreet) advancedVertexArray[row - 1][column]);
        }
        if (column > 0 && advancedVertexArray[row][column - 1] instanceof AdvancedStreet) {
            neighbours.add((AdvancedStreet) advancedVertexArray[row][column - 1]);
        }
        if (row < advancedVertexArray.length - 1 && advancedVertexArray[row + 1][column] instanceof AdvancedStreet) {
            neighbours.add((AdvancedStreet) advancedVertexArray[row + 1][column]);
        }
        if (column < advancedVertexArray[0].length - 1 && advancedVertexArray[row][column + 1] instanceof AdvancedStreet) {
            neighbours.add((AdvancedStreet) advancedVertexArray[row][column + 1]);
        }
        return neighbours;
    }


    /**
     * Checks if the 4th requirement of a skeleton from the lecture is met
     * @param street the street to be checked
     * @return true if requirement is met, false otherwise
     */
    private boolean skeletonReq4(AdvancedStreet street) {
        boolean[][] parts = street.getParts();
        int row = street.getPosition().getRow();
        int column = street.getPosition().getColumn();

        if (row > 0 && advancedVertexArray[row - 1][column] instanceof AdvancedStreet) {
            boolean[][] neighbourParts = ((AdvancedStreet) advancedVertexArray[row - 1][column]).getParts();
            for (int i = 0; i < 3; i++) {
                if (parts[0][i] && !neighbourParts[2][i]) {
                    return false;
                }
            }
        } else if (row > 0 && parts[0][0] || parts[0][1] || parts[0][2]) {
            return false;
        }
        if (row < advancedVertexArray.length - 1 && advancedVertexArray[row + 1][column] instanceof AdvancedStreet) {
            boolean[][] neighbourParts = ((AdvancedStreet) advancedVertexArray[row + 1][column]).getParts();
            for (int i = 0; i < 3; i++) {
                if (parts[2][i] && !neighbourParts[0][i]) {
                    return false;
                }
            }
        } else if (row < advancedVertexArray.length - 1 && parts[2][0] || parts[2][1] || parts[2][2]) {
            return false;
        }

        if (column > 0 && advancedVertexArray[row][column - 1] instanceof AdvancedStreet) {
            boolean[][] neighbourParts = ((AdvancedStreet) advancedVertexArray[row][column - 1]).getParts();
            for (int i = 0; i < 3; i++) {
                if (parts[i][0] && !neighbourParts[i][2]) {
                    return false;
                }
            }
        } else if (column > 0 && parts[0][0] || parts[1][0] || parts[2][0]) {
            return false;
        }

        if (column < advancedVertexArray[0].length - 1 && advancedVertexArray[row][column + 1] instanceof AdvancedStreet) {
            boolean[][] neighbourParts = ((AdvancedStreet) advancedVertexArray[row][column + 1]).getParts();
            for (int i = 0; i < 3; i++) {
                if (parts[i][2] && !neighbourParts[i][0]) {
                    return false;
                }
            }
        } else if (column < advancedVertexArray[0].length - 1 && parts[0][2] || parts[1][2] || parts[2][2]) {
            return false;
        }

        return true;
    }


    /**
     * Lists the most frequent type of vertex objects in the advanced map
     * @return a sorted list of the most frequent type of vertex objects
     */
    public ArrayList<? extends AdvancedVertex> listMostFrequentType() {
        int streetCount = 0;
        int buildingCount = 0;
        int greenCount = 0;

        for (AdvancedVertex[] advancedVertices : advancedVertexArray) {
            for (AdvancedVertex vertex : advancedVertices) {
                if (vertex instanceof AdvancedStreet) {
                    streetCount++;
                } else if (vertex instanceof AdvancedBuilding) {
                    buildingCount++;
                } else if (vertex instanceof AdvancedGreen) {
                    greenCount++;
                }
            }
        }

        ArrayList<AdvancedVertex> mostFrequentType = new ArrayList<>();
        if (streetCount > buildingCount && streetCount > greenCount) {
            for (AdvancedVertex[] advancedVertices : advancedVertexArray) {
                for (AdvancedVertex advancedVertex : advancedVertices) {
                    if (advancedVertex instanceof AdvancedStreet) {
                        mostFrequentType.add(advancedVertex);
                    }
                }
            }
        } else if (buildingCount > streetCount && buildingCount > greenCount) {
            for (AdvancedVertex[] advancedVertices : advancedVertexArray) {
                for (AdvancedVertex advancedVertex : advancedVertices) {
                    if (advancedVertex instanceof AdvancedBuilding) {
                        mostFrequentType.add(advancedVertex);
                    }
                }
            }
        } else if (greenCount > streetCount && greenCount > buildingCount) {
            for (AdvancedVertex[] advancedVertices : advancedVertexArray) {
                for (AdvancedVertex advancedVertex : advancedVertices) {
                    if (advancedVertex instanceof AdvancedGreen) {
                        mostFrequentType.add(advancedVertex);
                    }
                }
            }
        }

        mostFrequentType.sort(Comparator.comparingInt(AdvancedVertex::getValue));

        return mostFrequentType;
    }


    /**
     * @return the advanced vertex array
     */
    public AdvancedVertex[][] getAdvancedVertexArray() {
        return advancedVertexArray;
    }

    /**
     * Sets the advanced vertex array
     * @param advancedVertexArray array to be set
     */
    public void setAdvancedVertexArray(AdvancedVertex[][] advancedVertexArray) {
        this.advancedVertexArray = advancedVertexArray;
    }
}
