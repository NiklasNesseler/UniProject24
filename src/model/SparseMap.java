package model;

import model.abstractClasses.DensityChecker;

import java.util.ArrayList;

public class SparseMap extends BasicMap implements DensityChecker {
    private BasicVertex[][] sparseVertexArray;

    public SparseMap(int[][] baseData) {
        super(baseData);
        initSparseVertexArray();
    }

    private void initSparseVertexArray() {
        sparseVertexArray = getVertexArray();
    }

    void putHospitals(ArrayList<Position2D> hospitals) {

    }

    @Override
    public void checkDensity() {

    }
}
