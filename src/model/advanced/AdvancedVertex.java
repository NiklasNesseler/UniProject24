package model.advanced;

import model.BasicVertex;

public class AdvancedVertex extends BasicVertex {
    private boolean[][] parts;
//    private AdvancedMap containingAdvancedMap;
    
    public AdvancedVertex(int row, int column, int value) {
        super(row, column, value);
        initParts();
    }

    private void initParts() {
        parts = new boolean[][]{
                {false, false, false},
                {false, true, false},
                {false, false, false}
        };
    }

    private void rotate90() {
        int n = parts.length;
        boolean[][] newParts = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newParts[j][n - 1 - i] = parts[i][j];
            }
        }
    }
}
