package model.advanced;

import java.util.Arrays;

public class AdvancedBuilding extends AdvancedVertex {
    public AdvancedBuilding(int row, int column, int value) {
        super(row, column, value);

        initParts();
    }

    @Override
    void initParts() {
        for (boolean[] part : parts) {
            Arrays.fill(part, true);
        }
    }
}
