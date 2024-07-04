package model.distances;

import model.BasicStreet;
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
    public void initGrid() {
        this.grid = new DistanceDurationGrid(sparseMap);
    }

    /**
     * Computes the spatial distance between 2 vertices
     * @param start vertex
     * @param end vertex
     * @return the spatial distance in int
     */
    public int computeDistance(BasicVertex start, BasicVertex end) {
        if (!sparseMap.areStreetConnected(start, end)) {
            return Integer.MAX_VALUE;
        }

        Queue<BasicVertex> queue = new LinkedList<>();
        Map<BasicVertex, Integer> distances = new HashMap<>();
        Set<BasicVertex> visited = new HashSet<>();

        queue.offer(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            BasicVertex current = queue.poll();
            visited.add(current);

            if (current.equals(end)) {
                return distances.get(current);
            }

            for (BasicVertex neighbour : current.getNeighbours()) {
                if (!visited.contains(neighbour) && (neighbour instanceof BasicStreet || neighbour.equals(end))) {
                    int newDistance = distances.get(current) + grid.getDistances().get(current).get(neighbour);
                    if (!distances.containsKey(neighbour) || newDistance < distances.get(neighbour)) {
                        distances.put(neighbour, newDistance);
                        queue.offer(neighbour);
                    }
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    /**
     * Computes the temporal distance between 2 vertices
     * @param start vertex
     * @param end vertex
     * @return the temporal distance between 2 vertices
     */

    public int computeDuration(BasicVertex start, BasicVertex end) {
        ArrayList<ArrayList<BasicVertex>> paths = getPath(start, end);

        int duration = Integer.MAX_VALUE;
        for (ArrayList<BasicVertex> path : paths) {
            int tripDuration1 = computeDurationOfTrip(path);
            if (duration > tripDuration1) {
                duration = tripDuration1;
            }
        }
        return duration;
    }

    /**
     * Getting every possible path between start and end and filtering out all of those, that are not connected by streets
     * using a dfs
     * @param start start vertex
     * @param end end vertex
     * @return all possible paths
     */
    private ArrayList<ArrayList<BasicVertex>> getPath(BasicVertex start, BasicVertex end) {
        ArrayList<ArrayList<BasicVertex>> allPaths = new ArrayList<>();

        if (start.getNeighbours().contains(end)) {
            ArrayList<BasicVertex> directPath = new ArrayList<>();
            directPath.add(start);
            directPath.add(end);
            allPaths.add(directPath);
            return allPaths;
        }

        ArrayList<BasicVertex> currentPath = new ArrayList<>();
        Set<BasicVertex> visited = new HashSet<>();
        dfs(start, end, currentPath, visited, allPaths);


        return allPaths;
    }

    private void dfs(BasicVertex current, BasicVertex end, ArrayList<BasicVertex> currentPath,
                     Set<BasicVertex> visited, ArrayList<ArrayList<BasicVertex>> allPaths) {


        currentPath.add(current);
        visited.add(current);

        if (current.equals(end)) {
            if (sparseMap.isBasicPathOverStreets(currentPath)) {
                allPaths.add(new ArrayList<>(currentPath));
            }
        } else {
            for (BasicVertex neighbour : current.getNeighbours()) {
                if (!visited.contains(neighbour)) {
                        dfs(neighbour, end, currentPath, visited, allPaths);
                    }
                }
            }


        currentPath.removeLast();
        visited.remove(current);
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
            //int duration = computeDuration(current, next);

            int duration = grid.getDurations().get(current).get(next);

            if (duration == Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            totalDuration += duration;
        }
        return totalDuration;
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