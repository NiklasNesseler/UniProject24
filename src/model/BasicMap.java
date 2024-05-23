package model;

import model.abstractClasses.BasicMapTemplate;

import java.util.*;

/**
 * Die Klasse BasicMap stellt eine Kartenstruktur dar, in der verschiedene Arten von Knoten
 * wie Gebäude, Straßen und Grünflächen organisiert sind. Diese Klasse erbt von BasicMapTemplate
 * und implementiert spezifische Funktionen für das Initialisieren und Verwalten der Knoten in der Karte.
 *
 * Die Klasse verwendet ein zweidimensionales Array von BasicVertex-Objekten, das als vertexArray bezeichnet wird.
 * Jeder Knoten in diesem Array kann ein BasicVertex sein oder von BasicVertex abgeleitete Klassen wie BasicStreet,
 * BasicBuilding oder BasicGreen. Diese Klassifizierung basiert auf den Daten, die im Konstruktor übergeben werden.
 *
 *
 *
 */

public class BasicMap extends BasicMapTemplate {
    public BasicMap(int[][] baseData) {
        super(baseData);
        initVertexArray(baseData);

    }

    @Override
    public void initVertexArray(int[][] baseData) {
        //8.3 a)
        BasicVertex[][] vertexArray;
        vertexArray = new BasicVertex[baseData.length][baseData[0].length];
        for (int i = 0; i < baseData.length; i++) {
            for (int j = 0; j < baseData[i].length; j++) {
                switch (baseData[i][j]) {
                    case 1:
                        vertexArray[i][j] = new BasicVertex(i, j, -1);
                        break;
                    case 2:
                        vertexArray[i][j] = new BasicStreet(i, j, -1, -1);
                        break;
                    case 3:
                        vertexArray[i][j] = new BasicBuilding(i, j, -1, -1);
                        break;
                    case 4:
                        vertexArray[i][j] = new BasicGreen(i, j, -1);
                        break;
                    default:
                        vertexArray[i][j] = new BasicVertex(i, j, -1);
                        break;
                }

            }
        }

        super.setVertexArray(vertexArray);

    }

    @Override
    public void putValuesToBasicMap(int[][] valueArray) {
        for (int row = 0; row < getVertexArray().length; row++) {
            for (int column = 0; column < getVertexArray()[0].length; column++) {
                getVertexArray()[row][column].setValue(valueArray[row][column]);
            }
        }

    }

    @Override
    public Optional<BasicVertex> getBasicVertex(Position2D pos) {
        if (pos.getRow() >= 0 && pos.getRow() < getVertexArray().length && pos.getColumn() >= 0 && pos.getColumn() < getVertexArray()[0].length) {
            return Optional.of(getVertexArray()[pos.getRow()][pos.getColumn()]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int countBasicVerticesWithValue(int vertexValue) {
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex.getValue() == vertexValue) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countBasicBuildings() {
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicBuilding) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countBasicStreets() {
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countBasicGreens() {
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicGreen) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public boolean isBasicPathOverStreets(ArrayList<BasicVertex> vertexList) {
        if (vertexList == null || vertexList.isEmpty() || vertexList.size() < 3) {
            return false;
        }

        int size = vertexList.size();

        BasicVertex first = vertexList.get(0);
        BasicVertex last = vertexList.get(size - 1);


        if (!(isValidEndVertex(first) &&isValidEndVertex(last))) {
            return false;

        }



//        for (int i = 1; i < size - 1; i++) {
//            BasicVertex current = vertexList.get(i);
//            if (!(current instanceof BasicStreet)) {
//                return false;
//            }
//        }

        Set<BasicVertex> visited = new HashSet<>();
        visited.add(first);

        for (int i = 0; i < size - 1; i++) {

            BasicVertex current = vertexList.get(i);
            BasicVertex next = vertexList.get(i + 1);
            visited.add(current);
            if (i != 0 && visited.contains(next) && !(i == size - 2 && next == first && first == last))  {
                return false;
            }

            if (!current.isBasicStreetConnectedTo(next)) {
                return false;
            }

            if (i > 0 && i < vertexList.size() - 1 && !(current instanceof BasicStreet)) {
                return false;
            }

        }


            return true;
        }

        private boolean isValidEndVertex(BasicVertex vertex) {
            return vertex instanceof BasicStreet || vertex instanceof BasicGreen || vertex instanceof BasicBuilding;
        }


        @Override
        public boolean isBasicPathByValue(int value) {
            List<BasicVertex> verticesWithValue = new ArrayList<>();
            for (BasicVertex[] row : getVertexArray()) {
                for (BasicVertex vertex : row) {
                    if (vertex.getValue() == value) {
                        verticesWithValue.add(vertex);
                    }
                }
            }
            if (verticesWithValue.isEmpty())
                return false;

            Set<BasicVertex> visited = new HashSet<>();
            Queue<BasicVertex> q = new LinkedList<>();
            q.add(verticesWithValue.get(0));
            visited.add(verticesWithValue.get(0));

            //BFS
            while (!q.isEmpty()) {
                BasicVertex current = q.poll();
                visited.add(current);
                boolean hasNeighbours = false;
                for (BasicVertex neighbour : current.getNeighbours()) {
                    if (neighbour.getValue() == value && !visited.contains(neighbour)) {
                        q.add(neighbour);
                        hasNeighbours = true;
                    }
                }

                if (!hasNeighbours) {
                    break;
                }
            }
            return visited.size() == verticesWithValue.size();
        }


    @Override
    public boolean hasValidBuildingPlacements() {
        BasicVertex[][] vertexArray = getVertexArray();

        for (BasicVertex[] row : vertexArray) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicBuilding) {
                    boolean hasAdjacentStreets = false;
                    for (BasicVertex neighbour : vertex.getNeighbours()) {
                        if (neighbour instanceof BasicStreet) {
                            hasAdjacentStreets = true;
                            break;
                        }
                    }
                    if (!hasAdjacentStreets) {
                        return false;
                    }
                }
                }
            }

            return true;
        }



    @Override
    public boolean isValidBasicMap() {
        BasicVertex[][] vertexArray = getVertexArray();

        for (BasicVertex[] row : vertexArray) {
            for (BasicVertex vertex : row) {
                if (vertex == null || vertex.getValue() < 1) {
                    return false;
                }
            }
        }
        if (!hasValidBuildingPlacements()) {
            return false;
    }
        return areAllStreetsConnected();
    }

    private boolean areAllStreetsConnected() {
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    if (!isConnectedToAnotherStreet(vertex)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isConnectedToAnotherStreet(BasicVertex street) {
        List<BasicVertex> neighbours = street.getNeighbours();

        for (BasicVertex neighbour : neighbours) {
            if (neighbour instanceof BasicStreet) {
                return true;
            }
        }
        return false;
    }
}
