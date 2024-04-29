package model;

public class BasicStreet extends BasicVertex{
    private int speedLimit;
    public BasicStreet(int row, int column, int value) {
        super(row, column, value);
    }

    public void isDeadEnd(){

    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }
}
