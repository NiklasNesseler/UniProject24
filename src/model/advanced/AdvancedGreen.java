package model.advanced;

import java.util.Arrays;

/**
 * Represents a Green in an advanced Map, extending Advanced Vertex
 */
public class AdvancedGreen extends AdvancedVertex{

    /**
     * Constructor for the AdvancedGreen
     * @param row row of the green
     * @param column column of the green
     * @param value value of the green
     */
    public AdvancedGreen(int row, int column, int value) {
        super(row, column, value);
        initParts();
    }

    /**
     * Initializes the parts representing a green
     */
    @Override
    public void initParts() {
        for (boolean[] part : getParts()) {
            Arrays.fill(part, false);
        }
    }

}
