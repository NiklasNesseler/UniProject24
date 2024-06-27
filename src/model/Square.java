package model;

import model.abstractClasses.DensityChecker;

import java.util.ArrayList;
import java.util.List;

public class Square implements DensityChecker, Comparable<Square> {
    private BasicVertex[] squareMembers;

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

    private boolean isOnRightOrBottomEdge(BasicVertex basicVertex) {
        int row = basicVertex.getPosition().getRow();
        int column = basicVertex.getPosition().getColumn();
        SparseMap map = (SparseMap) basicVertex.getContainingMap();
        return (row >= map.getVertexArray().length - 1 || column >= map.getVertexArray()[0].length - 1);
    }

    public void initSquareMembers(BasicVertex basicVertex) {
        squareMembers[0] = basicVertex;
        squareMembers[1] = findRightNeighbour(basicVertex);
        squareMembers[2] = findBottomNeighbour(squareMembers[1]);
        squareMembers[3] = findBottomNeighbour(basicVertex);
    }

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


    private int countStreetVertices() {
        int count = 0;
        for (BasicVertex vertex : squareMembers) {
            if (vertex instanceof BasicStreet) {
                count++;
            }
        }
        return count;

    }

    public BasicVertex[] getSquareMembers() {
        return squareMembers;
    }

    public void setSquareMembers(BasicVertex[] squareMembers) {
        this.squareMembers = squareMembers;
    }

    @Override
    public boolean isSparse() {
        return countStreetVertices() <= 3;
    }
}
