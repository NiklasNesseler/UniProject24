package model.distances;

import model.BasicVertex;
import model.SparseMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
        //nutze DistanceDurationGrid methoden
        LinkedHashMap<BasicVertex, Integer> startDistance = grid.getDistances().get(start);
        if (startDistance != null && startDistance.containsKey(end)) {
            return startDistance.get(end);
        }
        return grid.shortestSpatialPath(start, end);
    }

    public int computeDuration(BasicVertex start, BasicVertex end) {
        //nutze DistanceDurationGrid methoden
        LinkedHashMap<BasicVertex, Integer> startDuration = grid.getDurations().get(start);
        if (startDuration != null && startDuration.containsKey(end)) {
            return startDuration.get(end);
        }
        return grid.shortestTemporalPath(start, end);
    }

    public int computeDurationOfTrip(ArrayList<BasicVertex> vertexList) {
        int fullDuration = 0;

        for (int i = 0; i < vertexList.size() - 1; i++) {
            BasicVertex current = vertexList.get(i);
            BasicVertex next = vertexList.get(i + 1);
            int duration = computeDuration(current, next);
            if (duration == Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }

            fullDuration += duration;

        }

        return fullDuration;

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