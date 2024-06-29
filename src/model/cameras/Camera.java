package model.cameras;

import model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Camera {
    private int range;
    private Position2D position;
    private SparseMap observedMap;
    private ArrayList<BasicVertex> observedVertices;

    public Camera(int range, Position2D position, SparseMap observedMap) {
        this.range = range;
        this.position = position;
        this.observedMap = observedMap;
        this.observedVertices = new ArrayList<>();
        initObservedVertices();
    }

    void initObservedVertices() {
        BasicVertex[][] vertexArray = observedMap.getSparseVertexArray();
        BasicVertex cameraVertex = vertexArray[position.getRow()][position.getColumn()];

//        if (!(cameraVertex instanceof BasicStreet)) {
//            throw new IllegalArgumentException("Cameras can only be placed on street nodes");
//        }

        for (int negativeRangeRow = -range; negativeRangeRow <= range; negativeRangeRow++) {
            for (int negativeRangeCol = -range; negativeRangeCol <= range; negativeRangeCol++) {
                int currentRow = position.getRow() + negativeRangeRow;
                int currentColumn = position.getColumn() + negativeRangeCol;

                if (isInBound(currentRow, currentColumn, vertexArray)) {
                    BasicVertex current = vertexArray[currentRow][currentColumn];
                    if (cameraVertex.getBasicManhattanDistance(current) <= range && isVisible(current, vertexArray)) {
                        observedVertices.add(current);
                    }
                }
            }
        }
        observedVertices.sort(Comparator.comparingInt(BasicVertex::getValue));
    }

    private boolean isVisible(BasicVertex current, BasicVertex[][] basicVertexArray) {
        if (current instanceof BasicBuilding) {
            return false;
        }
        int cameraRow = position.getRow();
        int cameraColumn = position.getColumn();
        int endRow = current.getPosition().getRow();
        int endColumn = current.getPosition().getColumn();

        if (cameraRow == endRow) {
            int minCol = Math.min(cameraColumn, endColumn);
            int maxCol = Math.max(cameraColumn, endColumn);
            for (int col = minCol + 1; col < maxCol; col++) {
                if (basicVertexArray[cameraRow][col] instanceof BasicBuilding) {
                    return false;
                }
            }
        }
        else if (cameraColumn == endColumn) {
            int minRow = Math.min(cameraRow, endRow);
            int maxRow = Math.max(cameraRow, endRow);
            for (int row = minRow + 1; row < maxRow; row++) {
                if (basicVertexArray[row][cameraColumn] instanceof BasicBuilding) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean isInBound(int currentRow, int currentColumn, BasicVertex[][] vertexArray) {
        return currentRow >= 0 && currentRow < vertexArray.length && currentColumn >= 0 && currentColumn < vertexArray[0].length;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Position2D getPosition() {
        return position;
    }

    public void setPosition(Position2D position) {
        this.position = position;
    }

    public SparseMap getObservedMap() {
        return observedMap;
    }

    public void setObservedMap(SparseMap observedMap) {
        this.observedMap = observedMap;
    }

    public ArrayList<BasicVertex> getObservedVertices() {
        return observedVertices;
    }

    public void setObservedVertices(ArrayList<BasicVertex> observedVertices) {
        this.observedVertices = observedVertices;
    }
}
