package model;

public class BasicBuilding extends BasicVertex{
    private int height;

    public BasicBuilding(int row, int column, int value, int height) {
        super(row, column, value);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
