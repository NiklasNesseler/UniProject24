package model.advanced;

import model.BasicStreet;
import model.BasicVertex;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AdvancedStreet extends AdvancedVertex {
    public enum StreetTypes {
        DUMMY,
        CROSSING,
        TJUNCTION,
        LINE,
        CURVE,
        NEEDLE
    }
    StreetTypes type;

    public AdvancedStreet(int row, int column, int value, StreetTypes type) {
        super(row, column, value);
        this.type = type;
        initParts(type);
    }

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



    public StreetTypes getType() {
        return type;
    }

    public void setType(StreetTypes type) {
        this.type = type;
    }
}
