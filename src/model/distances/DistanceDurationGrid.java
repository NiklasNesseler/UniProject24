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
                LinkedHashMap<BasicVertex, Integer> neighbourDistances = new LinkedHashMap<>();
                for (BasicVertex neighbour : vertex.getNeighbours()) {
                    int distance = spatialDistance(vertex, neighbour);
                    neighbourDistances.put(neighbour, distance);
                }
                distances.put(vertex, neighbourDistances);
            }
        }
    }

    private int spatialDistance(BasicVertex a, BasicVertex b) {
        if (a.equals(b)) {
            return 0;
        } else if (a instanceof BasicStreet && b instanceof BasicStreet && a.getNeighbours().contains(b)) {
            return 1200;
        } else if ((a instanceof BasicStreet || b instanceof BasicStreet) && a.getNeighbours().contains(b)) {
            return 600;
        } else if (!(a instanceof BasicStreet) && !(b instanceof BasicStreet) && a.getNeighbours().contains(b)) {
            return 30;
            
        } else if (a.isBasicStreetConnectedTo(b)) {
            return shortestSpatialPath(a, b);

        } else {
            return Integer.MAX_VALUE;
        }
    }

    private int shortestSpatialPath(BasicVertex a, BasicVertex b) {
        return shortestPathDjikstra(a, b, true);
    }

    private int shortestPathDjikstra(BasicVertex a, BasicVertex b, boolean isSpatial) {
        Map<BasicVertex, Integer> distances = new HashMap<>();
        PriorityQueue<BasicVertex> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Set<BasicVertex> visited = new HashSet<>();

        for (BasicVertex[] row : sparseMap.getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                distances.put(vertex, Integer.MAX_VALUE);
            }
        }

        distances.put(a, 0);
        queue.add(a);

        while (!queue.isEmpty()) {
            BasicVertex current = queue.poll();

            if (current.equals(b)) {
                return distances.get(current);
            }

            if (!visited.add(current)) {
                continue;
            }

            for (BasicVertex neighbour : current.getNeighbours()) {
                if (!(neighbour instanceof BasicStreet)) {
                    continue;
                }

                int newDistance = distances.get(current) + (isSpatial ? spatialDistance(current, neighbour) : temporalDistance(current, neighbour));

                if (newDistance < distances.get(neighbour)) {
                    distances.put(neighbour, newDistance);
                    queue.add(neighbour);
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    void initDistances() {
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

    private int temporalDistance(BasicVertex a, BasicVertex b) {
        if (a.equals(b)) {
            return 0;
        } else if (a instanceof BasicStreet streetA && b instanceof BasicStreet streetB && a.getNeighbours().contains(b)) {
            if (streetA.getSpeedLimit() == 0 && streetB.getSpeedLimit() == 0) {
                return Integer.MAX_VALUE;
            }
            else {
                return (600 / streetA.getSpeedLimit()) + (600 / streetB.getSpeedLimit());
            }
            
        } else if (a.getNeighbours().contains(b)) {
            return 5;

        } else if (a.isBasicStreetConnectedTo(b)) {
            return shortestTemporalPath(a, b);

        } else {
            return Integer.MAX_VALUE;
        }
    }

    private int shortestTemporalPath(BasicVertex a, BasicVertex b) {
        return shortestPathDjikstra(a, b, false);
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
