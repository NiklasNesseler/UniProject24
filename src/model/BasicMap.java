package model;

import model.abstractClasses.BasicMapTemplate;

import java.util.ArrayList;
import java.util.Optional;

public class BasicMap extends BasicMapTemplate {
    public BasicMap(int[][] baseData) {
        super(baseData);
        initVertexArray(baseData);

    }

    @Override
    public void initVertexArray(int[][] baseData) {
        //8.3 a)
        BasicVertex[][] vertexArray = super.getVertexArray();
        vertexArray = new BasicVertex[baseData.length][baseData[0].length];
        //TODO: 8.3 b) Verstehe ohne Spaß nicht was ihr hier von einem wollt. Formuliert das mal bitte anständig danke.


    }

    @Override
    public void putValuesToBasicMap(int[][] valueArray) {
        //give the values to the vertex array
        for (int row = 0; row < getVertexArray().length; row++) {
            for (int column = 0; column < getVertexArray()[0].length; column++) {
                getVertexArray()[row][column] = new BasicVertex(row, column, valueArray[row][column]);
            }
        }

    }

    @Override
    public Optional<BasicVertex> getBasicVertex(Position2D pos) {
        //return Optional if the position exists
        if (pos.getRow() >= 0 && pos.getRow() < getVertexArray().length && pos.getColumn() >= 0 && pos.getColumn() < getVertexArray()[0].length) {
            return Optional.of(getVertexArray()[pos.getRow()][pos.getColumn()]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int countBasicVerticesWithValue(int vertexValue) {
        //count the number of BasicVertices with vertexValue as their value
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
        //count the number of BasicBuildings
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
        //returns true if the whole path is a street except for the last one
        if (vertexList == null || vertexList.size() == 0) {
            return false;
        }
        for (int i = 0; i < vertexList.size() - 1; i++) {
            if (vertexList.get(i).isBasicStreetConnectedTo(vertexList.get(i + 1))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBasicPathByValue(int value) {
        //returns true if it is possible to arrange vertices with the given value in a single path
        //check if there is a path from one vertex with the given value to another with the same value
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex.getValue() == value) {
                    for (BasicVertex otherVertex : row) {
                        if (otherVertex.getValue() == value && vertex.getBasicManhattanDistance(otherVertex) == 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasValidBuildingPlacements() {
        //returns true if there is no BasicBuilding object in the vertex array or if every BasicBuilding has at least one adjacent BasicStreet
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (!(vertex instanceof BasicBuilding) || vertex.isBasicStreetConnectedTo(vertex)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isValidBasicMap() {
        //returns true if all vertices are not null, all vertices have a value of at least 1, hasValidBuildingPlacements() returns true, and every street is connected
        boolean isValid = true;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex == null || vertex.getValue() < 1) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid)
                break;
        }

        if (isValid && hasValidBuildingPlacements()) {
            for (BasicVertex[] row : getVertexArray()) {
                for (BasicVertex vertex : row) {
                    if (!vertex.isBasicStreetConnectedTo(vertex)) {
                        isValid = false;
                        break;
                    }
                }
                if (!isValid)
                    break;
            }

        }

        return isValid;
    }
}
