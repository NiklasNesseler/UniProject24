package model;

import model.abstractClasses.BasicVertexTemplate;

import java.util.*;

/**
 * Die Klasse BasicVertex repräsentiert einen Knoten einer größeren Struktur,
 * einer sogenannten BasicMap, ist. Diese Klasse erweitert BasicVertexTemplate und implementiert zusätzliche Funktionalitäten
 * zur Interaktion mit anderen Knoten in der Karte.
 *
 */

public class BasicVertex extends BasicVertexTemplate {
    public BasicVertex(int row, int column, int value) {
        super(row, column, value);
        initPosition(row, column);
    }

    @Override
    public void initPosition(int row, int column) {
        Position2D position2D = new Position2D(row, column);
        setPosition(position2D);
    }

    @Override
    public boolean isOnBound() {
        return (getPosition().getRow() == 0 || getPosition().getRow() == getContainingMap().getVertexArray().length - 1 ||
                getPosition().getColumn() == 0 || getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1);
    }

    @Override
    public boolean isOnCorner() {
        return (getPosition().getRow() == 0 && getPosition().getColumn() == 0) ||
               (getPosition().getRow() == 0 && getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1) ||
               (getPosition().getRow() == getContainingMap().getVertexArray().length - 1 && getPosition().getColumn() == 0) ||
               (getPosition().getRow() == getContainingMap().getVertexArray().length - 1 && getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1);
    }


    @Override
    public int getBasicManhattanDistance(BasicVertex v) {
        return Math.abs(getPosition().getRow() - v.getPosition().getRow()) + Math.abs(getPosition().getColumn() - v.getPosition().getColumn());
    }

    @Override
    public int getBasicDistance(BasicVertex v, int connectValue) {
        Map<BasicVertex, Integer> distances = traverse(connectValue);

        return distances.getOrDefault(v, -1);

    }

    public Map<BasicVertex, Integer> traverse(int requiredConnectValue) {
        Map<BasicVertex, Integer> distances = new HashMap<>();

        if (this.getValue() != requiredConnectValue) {
            return distances;
        }


        Queue<BasicVertex> q = new LinkedList<>();
        Set<BasicVertex> visited = new HashSet<>();

        q.add(this);
        distances.put(this, 0);
        visited.add(this);

        while (!q.isEmpty()) {
            BasicVertex currentVertex = q.poll();
            int currentDistance = distances.get(currentVertex);

            for (BasicVertex neighbor : currentVertex.getNeighbours()) {
                if (!visited.contains(neighbor) && neighbor.getValue() == requiredConnectValue) {
                    q.add(neighbor);
                    distances.put(neighbor, currentDistance + 1);
                    visited.add(neighbor);
                }

            }
        }
        return distances;
    }
    public List<BasicVertex> getNeighbours() {
        List<BasicVertex> neighbours = new ArrayList<>();
        int row = getPosition().getRow();
        int column = getPosition().getColumn();
        BasicVertex[][] map = getContainingMap().getVertexArray();

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


    @Override
    public boolean isBasicStreetConnectedTo(BasicVertex v) {
        return isBasicStreetConnectedTo(v, new HashSet<>());
    }

    public boolean isBasicStreetConnectedTo(BasicVertex v, Set <BasicVertex> blackList) {
        if (v == null) {
            return false;
        }
        Queue<BasicVertex> q = new LinkedList<>();
        Set<BasicVertex> visited = new HashSet<>();

        q.add(this);
        visited.add(this);

        while (!q.isEmpty()) {
            BasicVertex current = q.poll();
            visited.add(current);
            if (current.equals(v)) {
                return true;
            }

            for (BasicVertex neighbour : current.getNeighbours()) {
                if (!visited.contains(neighbour) && (neighbour instanceof BasicStreet || neighbour.equals(v)) && !blackList.contains(neighbour)) {
                    q.add(neighbour);

                }
            }
        }

        return false;
    }




}
