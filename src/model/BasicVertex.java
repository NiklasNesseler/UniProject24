package model;

import model.abstractClasses.BasicVertexTemplate;

public class BasicVertex extends BasicVertexTemplate {
    public BasicVertex(int row, int column, int value) {
        super(row, column, value);
        initPosition(row, column);
    }

    @Override
    public void initPosition(int row, int column) {
        Position2D position2D = new Position2D(row, column);
        setPosition(position2D);
    }

    @Override
    public boolean isOnBound() {
        //returns true if the vertex is on the edge of the map
        return (getPosition().getRow() == 0 || getPosition().getRow() == getContainingMap().getVertexArray().length - 1 ||
                getPosition().getColumn() == 0 || getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1);
    }

    @Override
    public boolean isOnCorner() {
        //returns true if the vertex is in a corner of the map
        return (getPosition().getRow() == 0 && getPosition().getColumn() == 0) ||
               (getPosition().getRow() == 0 && getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1) ||
               (getPosition().getRow() == getContainingMap().getVertexArray().length - 1 && getPosition().getColumn() == 0) ||
               (getPosition().getRow() == getContainingMap().getVertexArray().length - 1 && getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1);
    }


    @Override
    public int getBasicManhattanDistance(BasicVertex v) {
        //returns the shortest distance between our vertice and vertice v
        return Math.abs(getPosition().getRow() - v.getPosition().getRow()) + Math.abs(getPosition().getColumn() - v.getPosition().getColumn());
    }

    //TODO: Gibt die Länge eines kürzesten Weges unter bestimmten Bedingungen zurück
    @Override
    public int getBasicDistance(BasicVertex v, int connectValue) {
    }

    //TODO: Gibt true zurück, wenn der aufgerufene Knoten und Knoten v street connected sind
    @Override
    public boolean isBasicStreetConnectedTo(BasicVertex v) {

    }
}
