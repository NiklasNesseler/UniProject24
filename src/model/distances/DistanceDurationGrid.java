package model.distances;

import model.BasicStreet;
import model.BasicVertex;
import model.SparseMap;

import java.util.*;

/**
 * Class that computes the spatial and temporal distances between vertices in a sparse map
 */
public class DistanceDurationGrid {
    /**
     * Represents a sparse map where the calculations will be made
     */
    private SparseMap sparseMap;
    /**
     * Map storing the spatial distances between vertices
     */
    private LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> distances;
    /**
     * Map storing the temporal distances between vertices
     */
    private LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> durations;

    /**
     * Constructor for the Distance Duration Grid with the specified sparse map
     * @param sparseMap sparse map the grid is based on
     */
    public DistanceDurationGrid(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
        this.distances = new LinkedHashMap<>();
        this.durations = new LinkedHashMap<>();
        initDistances();
        initDurations();
    }

    /**
     * Initializes the map with the temporal distances
     */
    void initDurations() {
        BasicVertex[][] vertexArray = sparseMap.getSparseVertexArray();

        for (BasicVertex[] row : vertexArray) {
            for (BasicVertex vertex : row) {
                LinkedHashMap<BasicVertex, Integer> neighbourDurations = new LinkedHashMap<>();
                for (BasicVertex neighbour : vertex.getNeighbours()) {
                    int duration = temporalDistance(vertex, neighbour);
                    neighbourDurations.put(neighbour, duration);
                }
                durations.put(vertex, neighbourDurations);
            }
        }
    }

    /**
     * Initializes the spatial distances between vertices
     */
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

    /**
     * Calculates the spatial distance between two vertices
     * @param a vertice a
     * @param b vertice b
     * @return the spatial difference as an int
     */
    private int spatialDistance(BasicVertex a, BasicVertex b) {
        if (a.equals(b)) {
            return 0;
        }
        if (!a.isBasicStreetConnectedTo(b)) {
            return Integer.MAX_VALUE;
        }

        if (a instanceof BasicStreet && b instanceof BasicStreet) {
            return 1200;
        }
        if (a instanceof BasicStreet || b instanceof BasicStreet) {
            return 600;
        }

        return 30;
    }



    /**
     * Calculates the temporal distances between two vertices
     * @param a vertice a
     * @param b vertice b
     * @return the temporal distance as an int
     */
    int temporalDistance(BasicVertex a, BasicVertex b) {
        if (a.equals(b)) {
            return 0;
        }
        if (!(a instanceof BasicStreet) || !(b instanceof BasicStreet)) {
            return 5;
        }

        int sa = ((BasicStreet) a).getSpeedLimit();
        int sb = ((BasicStreet) b).getSpeedLimit();
        // fall c
        if (sa == 0 || sb == 0) {
            return Integer.MAX_VALUE;
        }
        return 600 / sa + 600 / sb;
    }

    /**
     * @return the sparse map used by the grid
     */
    public SparseMap getSparseMap() {
        return sparseMap;
    }

    /**
     * Sets the sparse map used by the grid
     * @param sparseMap the new sparse map
     */
    public void setSparseMap(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
    }

    /**
     * @return the spatial distances between two vertices
     */
    public LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> getDistances() {
        return distances;
    }

    /**
     * Sets the spatial distance between two vertices
     * @param distances vertice distances
     */
    public void setDistances(LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> distances) {
        this.distances = distances;
    }

    /**
     * @return the temporal distances between 2 vertices
     */
    public LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> getDurations() {
        return durations;
    }

    /**
     * Sets the temporal distances between 2 vertices
     * @param durations between 2 vertices
     */
    public void setDurations(LinkedHashMap<BasicVertex, LinkedHashMap<BasicVertex, Integer>> durations) {
        this.durations = durations;
    }

}