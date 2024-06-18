package model.distances;

import model.BasicVertex;
import model.SparseMap;

import java.util.LinkedHashMap;

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
    }

    void initDistances() {
    }
}
