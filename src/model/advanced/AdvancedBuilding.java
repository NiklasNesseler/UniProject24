package model.advanced;

import java.util.Arrays;

/**
 * Represents a Building in an advanced Map, extending Advanced Vertex
 */
public class AdvancedBuilding extends AdvancedVertex {

    /**
     * Constructor for the AdvancedBuilding
     * @param row row of the building
     * @param column column of the building
     * @param value height of the building
     */
    public AdvancedBuilding(int row, int column, int value) {
        super(row, column, value);

        initParts();
    }

    /**
     * Initializes the parts representing a building
     */
    @Override
    void initParts() {
        for (boolean[] part : parts) {
            Arrays.fill(part, true);
        }
    }
}
