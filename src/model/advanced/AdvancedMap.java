package model.advanced;

import model.*;

import java.lang.reflect.Array;
import java.util.*;

public class AdvancedMap{
    private AdvancedVertex[][] advancedVertexArray;

    public AdvancedMap(BasicMap basicMap) {
        initRefinement(basicMap);
    }

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

    public void buildAdvancedStreets(int[][] streetCodes) {
        for (int i = 0; i < streetCodes.length; i++) {
            for (int j = 0; j < streetCodes[i].length; j++) {
                AdvancedVertex vertex = advancedVertexArray[i][j];
                if (vertex instanceof AdvancedStreet street && ((AdvancedStreet) vertex).type == AdvancedStreet.StreetTypes.DUMMY) {
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

                    street.type = newStreetType;
                    street.initParts(newStreetType);

                    for (int k = 0; k < y; k++) {
                        street.rotate90();
                    }
                }
            }
        }
    }

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

            //Hier auch DUMMY verboten?
            if (current.type.equals(AdvancedStreet.StreetTypes.CROSSING) || current.type.equals(AdvancedStreet.StreetTypes.TJUNCTION)) {
                return false;
            }

            if (!areStreetsConnected(current, next)) {
                return false;
            }

        }
        return true;
    }

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

    public boolean isAdvancedCircle(ArrayList<AdvancedStreet> streetList) {
        if (streetList == null || streetList.isEmpty() || streetList.size() < 5) {
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

    public int countAdvancedCircles() {
        Set<AdvancedStreet> streets = new HashSet<>();

        for (AdvancedVertex[] advancedVertices : advancedVertexArray) {
            for (AdvancedVertex advancedVertex : advancedVertices) {
                if (advancedVertex instanceof AdvancedStreet) {
                    streets.add((AdvancedStreet) advancedVertex);
                }
            }
        }

        int circleCount = 0;
        Set<AdvancedStreet> visited = new HashSet<>();
        Set<AdvancedStreet> visitedCircles = new HashSet<>();
        Stack<AdvancedStreet> pathStack = new Stack<>();
        for (AdvancedStreet street : streets) {
            if (!visited.contains(street) && !visitedCircles.contains(street)) {
                if (dfsDetectCycle(street, null, visited, pathStack, visitedCircles)) {
                    ArrayList<AdvancedStreet> pathList = new ArrayList<>(pathStack);
                    if (isAdvancedCircle(pathList)) {
                        circleCount++;
                        printCycle(pathList);
                        visitedCircles.addAll(pathList);
                    }
                    pathStack.clear();
                }
            }
        }
        return circleCount;
    }

    private void printCycle(ArrayList<AdvancedStreet> pathStack) {
        System.out.println("Cycle detected:");
        for (AdvancedStreet street : pathStack) {
            System.out.print("(" + street.getPosition().getRow() + ", " + street.getPosition().getColumn() + ") ");
        }
        System.out.println();
    }

    private boolean dfsDetectCycle(AdvancedStreet current, AdvancedStreet parent, Set<AdvancedStreet> visited, Stack<AdvancedStreet> pathStack, Set<AdvancedStreet> visitedCircles) {
        visited.add(current);
        pathStack.push(current);

        for (AdvancedStreet neighbour : getAdvancedStreetNeighbours(current)) {
            if (neighbour != parent && !visitedCircles.contains(neighbour)) {
                if (visited.contains(neighbour)) {
                    pathStack.push(neighbour);
                    return true; // Potential cycle detected
                } else if (dfsDetectCycle(neighbour, current, visited, pathStack, visitedCircles)) {
                    return true; // Cycle detected in deeper call
                }
            }
        }

        pathStack.pop();
        return false;
    }

    private boolean isValidCycle(Stack<AdvancedStreet> pathStack) {
        ArrayList<AdvancedStreet> pathList = new ArrayList<>(pathStack);
        return isAdvancedCircle(pathList);
    }

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

        //1
        if (streets.size() < 2) {
            return false;
        }
        //2
        for (AdvancedStreet street : streets) {
            int count = 0;
            boolean[][] parts = street.getParts();
            for (boolean[] part : parts) {
                for (boolean b : part) {
                    if (b) {
                        count++;
                    }
                }
            }
            if (count < 2) {
                return false;
            }
        }

        //3 und 4
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


    public AdvancedVertex[][] getAdvancedVertexArray() {
        return advancedVertexArray;
    }

    public void setAdvancedVertexArray(AdvancedVertex[][] advancedVertexArray) {
        this.advancedVertexArray = advancedVertexArray;
    }
}
