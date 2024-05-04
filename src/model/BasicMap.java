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
        //TODO: 8.3 b)


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
        return 0;
    }

    @Override
    public int countBasicStreets() {
        return 0;
    }

    @Override
    public int countBasicGreens() {
        return 0;
    }

    @Override
    public boolean isBasicPathOverStreets(ArrayList<BasicVertex> vertexList) {
        return false;
    }

    @Override
    public boolean isBasicPathByValue(int value) {
        return false;
    }

    @Override
    public boolean hasValidBuildingPlacements() {
        return false;
    }

    @Override
    public boolean isValidBasicMap() {
        return false;
    }
}
