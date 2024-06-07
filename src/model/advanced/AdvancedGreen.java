package model.advanced;

import java.util.Arrays;

public class AdvancedGreen extends AdvancedVertex{
    public AdvancedGreen(int row, int column, int value) {
        super(row, column, value);
        initParts();
    }

    @Override
    void initParts() {
        for (boolean[] part : parts) {
            Arrays.fill(part, false);
        }
    }

}
