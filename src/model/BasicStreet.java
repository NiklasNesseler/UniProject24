package model;

public class BasicStreet extends BasicVertex{
    private int speedLimit;
    public BasicStreet(int row, int column, int value, int speedLimit) {
        super(row, column, value);
        this.speedLimit = speedLimit;

    }

//    public void isDeadEnd(){
//
//    }

    public boolean isBasicDeadEnd() {
        //TODO: Gibt dann true zurück, wenn es sich bei dem Knoten um ein Deadend handelt
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }
}
