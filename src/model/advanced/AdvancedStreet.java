package model.advanced;

import model.BasicStreet;
import model.BasicVertex;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a street in the advanced map
 */
public class AdvancedStreet extends AdvancedVertex {
    /**
     * Types of streets that can exist in the advanced map
     */
    public enum StreetTypes {
        DUMMY,
        CROSSING,
        TJUNCTION,
        LINE,
        CURVE,
        NEEDLE
    }
    private StreetTypes type;

    /**
     * Constructor of the AdvancedStreet with the specified parameters
     * @param row row of the street
     * @param column column of the street
     * @param value value / speed limit of the street
     * @param type type of the street
     */
    public AdvancedStreet(int row, int column, int value, StreetTypes type) {
        super(row, column, value);
        this.type = type;
        initParts(type);
    }


    /**
     * Initializes the parts array based on the type of the street
     * @param type type of the street
     */
    void initParts(StreetTypes type) {
        switch (type) {
            case DUMMY:
                parts = new boolean[][] {
                        {false, false, false},
                        {false, true, false},
                        {false, false, false}
                };
                break;
            case CROSSING:
                parts = new boolean[][] {
                        {false, true, false},
                        {true, true, true},
                        {false, true, false}
                };
                break;
            case TJUNCTION:
                parts = new boolean[][] {
                        {false, false, false},
                        {true, true, true},
                        {false, true, false}
                };
                break;
            case LINE:
                parts = new boolean[][] {
                        {false, false, false},
                        {true, true, true},
                        {false, false, false}
                };
                break;
            case CURVE:
                parts = new boolean[][] {
                        {false, true, false},
                        {true, true, false},
                        {false, false, false}
                };
                break;
            case NEEDLE:
                parts = new boolean[][] {
                        {false, false, false},
                        {true, true, false},
                        {false, false, false}
                };
                break;
        }
    }

    /**
     * Checks if a street is connected by streets to another one
     * @param v the vertex to check the connection with
     * @param blackList a set of streets to be avoided
     * @return true if the streets are connected, false otherwise
     */
    public boolean isAdvancedStreetConnectedTo(AdvancedVertex v, Set<AdvancedStreet> blackList){
        if (v == null) {
            return false;
        }
        Queue<AdvancedVertex> q = new LinkedList<>();
        Set<AdvancedVertex> visited = new HashSet<>();

        q.add(this);
        visited.add(this);

        while (!q.isEmpty()) {
            AdvancedVertex current = q.poll();
            visited.add(current);
            if (current.equals(v)) {
                return true;
            }

            for (AdvancedVertex neighbour : current.getAdvancedNeighbours()) {
                if (!visited.contains(neighbour) && (neighbour instanceof AdvancedStreet || neighbour.equals(v)) && !blackList.contains(neighbour)) {
                    q.add(neighbour);

                }
            }
        }

        return false;
    }


    /**
     * @return the street type
     */
    public StreetTypes getType() {
        return type;
    }

    /**
     * Sets the type of street
     * @param type the new type of the street
     */
    public void setType(StreetTypes type) {
        this.type = type;
    }
}
