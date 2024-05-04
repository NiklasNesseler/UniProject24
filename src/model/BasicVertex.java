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

    //TODO: Gibt true zurück wenn das Objekt am Rand der Karte ist
    @Override
    public boolean isOnBound() {
        return false;
    }


    //TODO: Gibt true zurück wenn das Objekt in der Kartenecke liegt
    @Override
    public boolean isOnCorner() {
        return false;
    }

    //TODO: Gibt die Manhatten Distanz zwischen dem aufrufenden Knoten und Knoten v zurück
    @Override
    public int getBasicManhattanDistance(BasicVertex v) {
        return 0;
    }

    //TODO: Gibt die Länge eines kürzesten Weges unter bestimmten Bedingungen zurück
    @Override
    public int getBasicDistance(BasicVertex v, int connectValue) {
        return 0;
    }

    //TODO: Gibt true zurück, wenn der aufgerufene Knoten und Knoten v street connected sind
    @Override
    public boolean isBasicStreetConnectedTo(BasicVertex v) {
        return false;
    }
}
