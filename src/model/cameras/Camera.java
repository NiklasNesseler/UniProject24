package model.cameras;

import model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Represents a surveillance camera in the map
 */
public class Camera {
    /**
     * represents the view range of the camera
     */
    private int range;
    /**
     * represents the position of the camera on the map
     */
    private Position2D position;
    /**
     * represents the observed map
     */
    private SparseMap observedMap;
    /**
     * ArrayList of vertices that are observed by the camera
     */
    private ArrayList<BasicVertex> observedVertices;

    /**
     * Constructor of the Camera with the specified range, position and observed map
     * @param range view range of the camera
     * @param position position of the camera
     * @param observedMap map that is being observed
     */
    public Camera(int range, Position2D position, SparseMap observedMap) {
        this.range = range;
        this.position = position;
        this.observedMap = observedMap;
        this.observedVertices = new ArrayList<>();
        initObservedVertices();
    }

    /**
     * Initializes the observedVertices list with the vertices that the camera can observe.
     * The vertices are added to the list if they are within the camera's range and visible.
     * The list is sorted by the value of the vertices.
     */
    void initObservedVertices() {
        BasicVertex[][] vertexArray = observedMap.getSparseVertexArray();
        BasicVertex cameraVertex = vertexArray[position.getRow()][position.getColumn()];

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

    /**
     * Checks if the current vertex is visible from the cameras position
     * @param current vertex to be checked for visibility
     * @param basicVertexArray the array of vertices in the map
     * @return true if current vertex is visible, false otherwise
     */
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

    /**
     * Checks if the given position is within the bounds of the vertex array
     * @param currentRow row index to check
     * @param currentColumn column index to check
     * @param vertexArray the array of vertices in the map
     * @return true if vertex is within bounds, false otherwise
     */
    private boolean isInBound(int currentRow, int currentColumn, BasicVertex[][] vertexArray) {
        return currentRow >= 0 && currentRow < vertexArray.length && currentColumn >= 0 && currentColumn < vertexArray[0].length;
    }

    /**
     * @return the range of the camera
     */
    public int getRange() {
        return range;
    }

    /**
     * @param range of the camera is set
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * @return the position of the camera in a map
     */
    public Position2D getPosition() {
        return position;
    }

    /**
     * @param position of the camera is set
     */
    public void setPosition(Position2D position) {
        this.position = position;
    }

    /**
     * @return the observed map
     */
    public SparseMap getObservedMap() {
        return observedMap;
    }

    /**
     * Set the observed map
     * @param observedMap observed by cameras map
     */
    public void setObservedMap(SparseMap observedMap) {
        this.observedMap = observedMap;
    }

    /**
     * @return the list of observed Vertices
     */
    public ArrayList<BasicVertex> getObservedVertices() {
        return observedVertices;
    }

    /**
     * @param observedVertices are set
     */
    public void setObservedVertices(ArrayList<BasicVertex> observedVertices) {
        this.observedVertices = observedVertices;
    }
}
