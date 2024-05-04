package model;

public class BasicBuilding extends BasicVertex{
    //TODO: Nichts. A6 erfüllt.
    private int height; //Speichert die Höhe des jeweiligen Gebäudes

    public BasicBuilding(int row, int column, int value, int height) {
        super(row, column, value);
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
