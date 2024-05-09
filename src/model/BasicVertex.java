package model;

import model.abstractClasses.BasicVertexTemplate;

import java.util.*;

/**
 * Die Klasse BasicVertex repräsentiert einen Knoten einer größeren Struktur,
 * einer sogenannten BasicMap, ist. Diese Klasse erweitert BasicVertexTemplate und implementiert zusätzliche Funktionalitäten
 * zur Interaktion mit anderen Knoten in der Karte.
 *
 * @author Niklas Nesseler
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
        //returns true if the vertex is on the edge of the map
        return (getPosition().getRow() == 0 || getPosition().getRow() == getContainingMap().getVertexArray().length - 1 ||
                getPosition().getColumn() == 0 || getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1);
    }

    @Override
    public boolean isOnCorner() {
        //returns true if the vertex is in a corner of the map
        return (getPosition().getRow() == 0 && getPosition().getColumn() == 0) ||
               (getPosition().getRow() == 0 && getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1) ||
               (getPosition().getRow() == getContainingMap().getVertexArray().length - 1 && getPosition().getColumn() == 0) ||
               (getPosition().getRow() == getContainingMap().getVertexArray().length - 1 && getPosition().getColumn() == getContainingMap().getVertexArray()[0].length - 1);
    }


    @Override
    public int getBasicManhattanDistance(BasicVertex v) {
        //returns the shortest distance between our vertice and vertice v
        return Math.abs(getPosition().getRow() - v.getPosition().getRow()) + Math.abs(getPosition().getColumn() - v.getPosition().getColumn());
    }

    @Override
    public int getBasicDistance(BasicVertex v, int connectValue) {
        Map<BasicVertex, Integer> distances = traverse(connectValue);

        return distances.getOrDefault(v, -1);

    }

    public Map<BasicVertex, Integer> traverse(int requiredConnectValue) {
        Queue<BasicVertex> q = new LinkedList<>();
        Map<BasicVertex, Integer> distances = new HashMap<>();
        Set<BasicVertex> visited = new HashSet<>();

        q.add(this);
        distances.put(this, 0);
        visited.add(this);

        while (!q.isEmpty()) {
            BasicVertex currentVertex = q.poll();
            int currentDistance = distances.get(currentVertex);

            for (BasicVertex neighbor : currentVertex.getNeighbors()) {
                if (!visited.contains(neighbor) && neighbor.getValue() == requiredConnectValue) {
                    q.add(neighbor);
                    distances.put(neighbor, currentDistance + 1);
                    visited.add(neighbor);
                }

            }
        }
        return distances;
    }
    public List<BasicVertex> getNeighbors() {
        List<BasicVertex> neighbours = new LinkedList<>();
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
        if (!(v instanceof BasicStreet) && !(this instanceof BasicStreet)) {
            return false;
        }
        Queue<BasicVertex> q = new LinkedList<>();
        Set<BasicVertex> visited = new HashSet<>();
        
        q.add(this);
        visited.add(this);
        
        while (!q.isEmpty()) {
            BasicVertex current = q.poll();
            if (current.equals(v)) {
                return true;
            }
        }
        
        for (BasicVertex neighbour : getNeighbors()) {
            if (!visited.contains(neighbour) && neighbour instanceof BasicStreet) {
                q.add(neighbour);
                visited.add(neighbour);
            }
        }

        return false;
    }
}
