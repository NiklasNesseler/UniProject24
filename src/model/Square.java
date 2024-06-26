package model;

import model.abstractClasses.DensityChecker;

import java.util.ArrayList;
import java.util.List;

public class Square implements DensityChecker, Comparable<Square> {
    private BasicVertex[] squareMembers;

    public Square(BasicVertex basicVertex) {
        squareMembers = new BasicVertex[4];
        initSquareMembers(basicVertex);
    }

    public void initSquareMembers(BasicVertex basicVertex) {
        squareMembers[0] = basicVertex;
        squareMembers[1] = findRightNeighbour(basicVertex);
        squareMembers[2] = findBottomNeighbour(squareMembers[1]);
        squareMembers[3] = findBottomNeighbour(basicVertex);
    }

    private BasicVertex findBottomNeighbour(BasicVertex vertex) {
        int row = vertex.getPosition().getRow() + 1;
        int column = vertex.getPosition().getColumn();

        return vertex.getContainingMap().getVertexArray()[row][column];

    }

    private BasicVertex findRightNeighbour(BasicVertex vertex) {
        int row = vertex.getPosition().getRow();
        int column = vertex.getPosition().getColumn() + 1;
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

    @Override
    public boolean isSparse() {
        return countStreetVertices() <= 3;
    }

    @Override
    public boolean isBasicStreetConnectedMap() {
        return false;
    }

    @Override
    public int countCommonVertices(ArrayList<BasicVertex> x, ArrayList<BasicVertex> y) {
        return 0;
    }

    @Override
    public boolean isSubtrip(ArrayList<BasicVertex> trip, ArrayList<BasicVertex> subtrip) {
        return false;
    }

    @Override
    public boolean isConnectedByValue(int connectValue) {
        return false;
    }

    @Override
    public boolean isCrucialPath(ArrayList<BasicVertex> vertexList) {
        return false;
    }

    @Override
    public boolean isClosedWorld() {
        return false;
    }

    @Override
    public boolean isCircle(ArrayList<BasicVertex> vertexList) {
        return false;
    }

    @Override
    public boolean isCircle() {
        return false;
    }

    public BasicVertex[] getSquareMembers() {
        return squareMembers;
    }

    public void setSquareMembers(BasicVertex[] squareMembers) {
        this.squareMembers = squareMembers;
    }

    @Override
    public boolean isTour(ArrayList<BasicVertex> vertexList, ArrayList<BasicVertex> stops) {
        return false;
    }
}
