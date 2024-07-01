package model.sites;

import model.BasicBuilding;

public class PoliceStation extends BasicBuilding {

    /**
     * Creates a new BasicBuilding object with specified position coordinates, a value and a height.
     * This constructor initializes the position and the value of the building via the superclass constructor
     * and sets the height of the building.
     *
     * @param row The row position of the building in the map.
     * @param column The column position of the building in the map.
     * @param value The value of the building, which can be used for various purposes.
     * @param height The height of the building in floors.
     */

    public PoliceStation(int row, int column, int value, int height) {
        super(row, column, value, height);
    }
}
