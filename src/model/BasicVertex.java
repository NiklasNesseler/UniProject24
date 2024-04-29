package model;

import model.abstractClasses.BasicVertexTemplate;

public class BasicVertex extends BasicVertexTemplate {
    public BasicVertex(int row, int column, int value) {
        super(row, column, value);
    }

    @Override
    public void initPosition(int row, int column) {

    }

    @Override
    public boolean isOnBound() {
        return false;
    }

    @Override
    public boolean isOnCorner() {
        return false;
    }

    @Override
    public int getBasicManhattanDistance(BasicVertex v) {
        return 0;
    }

    @Override
    public int getBasicDistance(BasicVertex v, int connectValue) {
        return 0;
    }

    @Override
    public boolean isBasicStreetConnectedTo(BasicVertex v) {
        return false;
    }
}
