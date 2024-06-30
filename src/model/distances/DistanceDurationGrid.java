package model.distances;

import model.BasicStreet;
import model.BasicVertex;
import model.SparseMap;

import java.util.*;

public class DistanceDurationGrid {
    private SparseMap sparseMap;
    private LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> distances;
    private LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> durations;

    public DistanceDurationGrid(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
        this.distances = new LinkedHashMap<>();
        this.durations = new LinkedHashMap<>();
        initDistances();
        initDurations();
    }

    void initDurations() {
        BasicVertex[][] vertexArray = sparseMap.getSparseVertexArray();

        for (BasicVertex[] row : vertexArray) {
            for (BasicVertex vertex : row) {
                LinkedHashMap<BasicVertex, Integer> neighbourDurations = new LinkedHashMap<>();
                for (BasicVertex neighbour : vertex.getNeighbours()) {
                    int duration = temporalDistance(vertex, neighbour);
                    neighbourDurations.put(neighbour, duration);
                }
                distances.put(vertex, neighbourDurations);
            }
        }
    }

    private int spatialDistance(BasicVertex a, BasicVertex b) {
        if (a.equals(b)) return 0;
        if (a instanceof BasicStreet && b instanceof BasicStreet) {
            return 1200;
        }
        if (a instanceof BasicStreet || b instanceof BasicStreet) {
            return 600;
        }
        return 30;
    }

    void initDistances() {
        BasicVertex[][] vertexArray = sparseMap.getSparseVertexArray();
        for (BasicVertex[] row : vertexArray) {
            for (BasicVertex vertex : row) {
                LinkedHashMap<BasicVertex, Integer> neighbourDistances = new LinkedHashMap<>();
                for (BasicVertex neighbour : vertex.getNeighbours()) {
                    int distance = spatialDistance(vertex, neighbour);
                    neighbourDistances.put(neighbour, distance);
                }
                distances.put(vertex, neighbourDistances);

            }
        }
    }

    private int temporalDistance(BasicVertex a, BasicVertex b) {
        if (a.equals(b)) return 0;
        if (!(a instanceof BasicStreet) || !(b instanceof BasicStreet)) {
            return 5;
        }
        int sa = ((BasicStreet) a).getSpeedLimit();
        int sb = ((BasicStreet) b).getSpeedLimit();
        if (sa == 0 || sb == 0) {
            return Integer.MAX_VALUE;
        }
        return 600 / sa + 600 / sb;
    }

    public SparseMap getSparseMap() {
        return sparseMap;
    }

    public void setSparseMap(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
    }

    public LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> getDistances() {
        return distances;
    }

    public void setDistances(LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> distances) {
        this.distances = distances;
    }

    public LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> getDurations() {
        return durations;
    }

    public void setDurations(LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> durations) {
        this.durations = durations;
    }
}