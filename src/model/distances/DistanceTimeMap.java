package model.distances;

import model.BasicVertex;
import model.SparseMap;

import java.util.*;

public class DistanceTimeMap {
    private SparseMap sparseMap;
    private DistanceDurationGrid grid;

    public DistanceTimeMap(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
        initGrid();
    }



    public void initGrid() {
        this.grid = new DistanceDurationGrid(sparseMap);
    }

    public int computeDistance(BasicVertex start, BasicVertex end) {
        return bfs(start, end, true);
    }

    public int computeDuration(BasicVertex start, BasicVertex end) {
        return bfs(start, end, false);
    }

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

    public int bfs(BasicVertex start, BasicVertex end, boolean isDistance) {
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


    public SparseMap getSparseMap() {
        return sparseMap;
    }

    public void setSparseMap(SparseMap sparseMap) {
        this.sparseMap = sparseMap;
    }

    public DistanceDurationGrid getGrid() {
        return grid;
    }

    public void setGrid(DistanceDurationGrid grid) {
        this.grid = grid;
    }
}