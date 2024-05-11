package model;

import java.util.List;

public class BasicStreet extends BasicVertex{
    private int speedLimit;
    public BasicStreet(int row, int column, int value, int speedLimit) {
        super(row, column, value);
        this.speedLimit = speedLimit;

    }

    public boolean isBasicDeadEnd() {
        //TODO: Gibt dann true zurück, wenn es sich bei dem Knoten um ein Deadend handelt
        List<BasicVertex> neighbours = this.getNeighbours();
        int connectedStreets = 0;
        for (BasicVertex neighbour : neighbours) {
            if (neighbour instanceof BasicStreet) {
                connectedStreets++;
            }
        }
        return connectedStreets <= 1;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }
}
