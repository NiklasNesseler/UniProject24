package model;

import model.abstractClasses.DensityChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a square on the map composed of four BasicVertex objects.
 */
public class Square implements DensityChecker, Comparable<Square> {
    /** An array of BasicVertex objects that make up the square. */
    private BasicVertex[] squareMembers;

    /**
     * Constructs a Square with the specified BasicVertex as the anchor point.
     * Initializes the square members by calling initSquareMembers(BasicVertex).
     *
     * @param basicVertex the anchor point of the square
     * @throws IllegalArgumentException if the basicVertex is null or on the right or bottom edge
     */
    public Square(BasicVertex basicVertex) {
        if (basicVertex == null) {
            throw new IllegalArgumentException("basicVertex cannot be null");
        }
        if (isOnRightOrBottomEdge(basicVertex)) {
            throw new IllegalArgumentException("BasicVertex cannot be on the right or bottom edge");
        }
        squareMembers = new BasicVertex[4];
        initSquareMembers(basicVertex);
    }

    /**
     * Checks if the specified BasicVertex is on the right or bottom edge of its containing map.
     *
     * @param basicVertex the BasicVertex to check
     * @return true if the basicVertex is on the right or bottom edge, false otherwise
     */
    private boolean isOnRightOrBottomEdge(BasicVertex basicVertex) {
        int row = basicVertex.getPosition().getRow();
        int column = basicVertex.getPosition().getColumn();
        SparseMap map = (SparseMap) basicVertex.getContainingMap();
        return (row >= map.getVertexArray().length - 1 || column >= map.getVertexArray()[0].length - 1);
    }

    /**
     * Initializes the square members with the specified anchor point.
     * The first member is the anchor point, the second is the right neighbor,
     * the third is the bottom neighbor of the right neighbor, and the fourth is the bottom neighbor of the anchor point.
     *
     * @param basicVertex the anchor point of the square
     */
    public void initSquareMembers(BasicVertex basicVertex) {
        squareMembers[0] = basicVertex;
        squareMembers[1] = findRightNeighbour(basicVertex);
        squareMembers[2] = findBottomNeighbour(squareMembers[1]);
        squareMembers[3] = findBottomNeighbour(basicVertex);
    }

    /**
     * Finds the bottom neighbor of the specified vertex.
     *
     * @param vertex the vertex to find the bottom neighbor for
     * @return the bottom neighbor of the vertex, or null if it doesn't exist
     */
    private BasicVertex findBottomNeighbour(BasicVertex vertex) {
        if (vertex==null) {
            return null;
        }
        int row = vertex.getPosition().getRow() + 1;
        int column = vertex.getPosition().getColumn();

        if (row >= vertex.getContainingMap().getVertexArray().length) {
            return null;
        }
        return vertex.getContainingMap().getVertexArray()[row][column];

    }

    /**
     * Finds the right neighbor of the specified vertex.
     *
     * @param vertex the vertex to find the right neighbor for
     * @return the right neighbor of the vertex, or null if it doesn't exist
     */
    private BasicVertex findRightNeighbour(BasicVertex vertex) {
        if (vertex == null) {
            return null;
        }
        int row = vertex.getPosition().getRow();
        int column = vertex.getPosition().getColumn() + 1;
        if (column >= vertex.getContainingMap().getVertexArray()[0].length) {
            return null;
        }
        return vertex.getContainingMap().getVertexArray()[row][column];
    }

    /**
     * Compares this square with another square based on the number of street vertices and anchor point values.
     *
     * @param o the other square to compare with
     * @return -1 if this square is less than the other square, 1 if greater, 0 if equal
     */
    @Override
    public int compareTo(Square o) {
        int street1 = countStreetVertices();
        int street2 = o.countStreetVertices();

        if (street1 < street2) {
            return -1;
        } else if (street1 > street2) {
            return 1;

        } else {
            int value1 = squareMembers[0].getValue();
            int value2 = o.squareMembers[0].getValue();
            return Integer.compare(value1, value2);
        }
    }


    /**
     * Counts the number of street vertices in the square.
     *
     * @return the number of street vertices
     */
    private int countStreetVertices() {
        int count = 0;
        for (BasicVertex vertex : squareMembers) {
            if (vertex instanceof BasicStreet) {
                count++;
            }
        }
        return count;

    }

    /**
     * @return the square members
     */
    public BasicVertex[] getSquareMembers() {
        return squareMembers;
    }



    /**
     * Sets the square members.
     *
     * @param squareMembers the new square members
     */
    public void setSquareMembers(BasicVertex[] squareMembers) {
        this.squareMembers = squareMembers;
    }

    /**
     * Checks if the square is sparse based on the number of street vertices.
     *
     * @return true if the square is sparse, false otherwise
     */
    @Override
    public boolean isSparse() {
        return countStreetVertices() <= 3;
    }
}
