package model.distances;

import model.BasicVertex;
import model.SparseMap;

import java.util.*;


/**
 * Class to compute spatial and temporal distances between vertices in a sparse map using DistanceDurationGrid
 */
public class DistanceTimeMap {
    /**
     * The sparse map where the grid is based on
     */
    private SparseMap sparseMap;
    /**
     * the grid that stores the distances and duration between vertices
     */
    private DistanceDurationGrid grid;

    /**
     * Constructor of the Distance time map with the specified sparse map
     * @param sparseMap sparse map the grid is based on
     */
    public DistanceTimeMap(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
        initGrid();
    }


    /**
     * Initializes the grid with a new distance duration grid based on the sparse map
     */
    void initGrid() {
        this.grid = new DistanceDurationGrid(sparseMap);
    }

    /**
     * Computes the spatial distance between 2 vertices
     * @param start vertex
     * @param end vertex
     * @return the spatial distance in int
     */
    public int computeDistance(BasicVertex start, BasicVertex end) {
        return bfs(start, end, true);
    }

    /**
     * Computes the temporal distance between 2 vertices
     * @param start vertex
     * @param end vertex
     * @return the temporal distance between 2 vertices
     */
    public int computeDuration(BasicVertex start, BasicVertex end) {
        return bfs(start, end, false);
    }

    /**
     * Computes the temporal duration of a trip represented by a list of vertices
     * @param vertexList list of
     * @return
     */
    public int computeDurationOfTrip(ArrayList<BasicVertex> vertexList) {
        int totalDuration = 0;
        for (int i = 0; i < vertexList.size() - 1; i++) {
            BasicVertex current = vertexList.get(i);
            BasicVertex next = vertexList.get(i + 1);
            int duration = computeDuration(current, next);
            if (duration == Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            totalDuration += duration;
        }
        return totalDuration;
    }


    /**
     * Performing a breadth first search to compute either the spatial or temporal distance between 2 vertices
     * @param start starting vertex
     * @param end ending vertex
     * @param isDistance true to compute spatial distance, false to compute temporal distance
     * @return the computed distance or duration between the start and end vertices
     */
    public int bfs(BasicVertex start, BasicVertex end, boolean isDistance) {
        if (!sparseMap.areStreetConnected(start, end)) {
            return Integer.MAX_VALUE;
        }
        Queue<BasicVertex> q = new LinkedList<>();
        HashMap<BasicVertex, Integer> distances = new HashMap<>();
        HashMap<BasicVertex, BasicVertex> p = new HashMap<>();

        q.offer(start);
        distances.put(start, 0);

        while (!q.isEmpty()) {
            BasicVertex current = q.poll();
            if (current.equals(end)) {
                return distances.get(current);
            }

            for (BasicVertex neighbour : current.getNeighbours()) {
                int weight = isDistance ?
                        grid.getDistances().get(current).get(neighbour) : grid.getDurations().get(current).get(neighbour);

                int newDistance = distances.get(current) + weight;
                if (!distances.containsKey(neighbour) || newDistance < distances.get(neighbour)) {
                    distances.put(neighbour, newDistance);
                    p.put(neighbour, current);
                    q.offer(neighbour);
                }

            }
        }
        return Integer.MAX_VALUE;

    }


    /**
     * @return the used sparse map
     */
    public SparseMap getSparseMap() {
        return sparseMap;
    }

    /**
     * Set the sparse map
     * @param sparseMap new sparse map
     */
    public void setSparseMap(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
    }

    /**
     * @return the distance duration grid
     */
    public DistanceDurationGrid getGrid() {
        return grid;
    }

    /**
     * Set the distance duration grid
     * @param grid specified new grid
     */
    public void setGrid(DistanceDurationGrid grid) {
        this.grid = grid;
    }
}