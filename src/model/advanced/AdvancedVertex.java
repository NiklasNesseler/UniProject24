package model.advanced;

import model.BasicStreet;
import model.BasicVertex;

import java.util.*;

public class AdvancedVertex extends BasicVertex {
    boolean[][] parts;
    private AdvancedMap containingAdvancedMap;
    
    public AdvancedVertex(int row, int column, int value) {
        super(row, column, value);
        parts = new boolean[3][3];
        initParts();
    }



    void initParts() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                parts[i][j] = (i == 1 && j == 1);
            }
        }
    }

    public void rotate90() {
        int n = parts.length;
        boolean[][] rotated = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = parts[i][j];
            }
        }
        parts = rotated;
    }

    public boolean[][] getParts() {
        return parts;
    }



    public void setParts(boolean[][] parts) {
        this.parts = parts;
    }

    public AdvancedMap getContainingAdvancedMap() {
        return containingAdvancedMap;
    }

    public void setContainingAdvancedMap(AdvancedMap containingAdvancedMap) {
        this.containingAdvancedMap = containingAdvancedMap;
    }

    public List<AdvancedVertex> getAdvancedNeighbours() {
        List<AdvancedVertex> neighbours = new ArrayList<>();
        int row = getPosition().getRow();
        int column = getPosition().getColumn();
        AdvancedVertex[][] map = getContainingAdvancedMap().getAdvancedVertexArray();

        if (row > 0) {
            neighbours.add(map[row - 1][column]);
        }
        if (row < map.length - 1) {
            neighbours.add(map[row + 1][column]);
        }
        if (column > 0) {
            neighbours.add(map[row][column - 1]);
        }
        if (column < map[0].length - 1) {
            neighbours.add(map[row][column + 1]);
        }

        return neighbours;
    }
}
