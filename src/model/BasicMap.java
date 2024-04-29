package model;

import model.abstractClasses.BasicMapTemplate;

import java.util.ArrayList;
import java.util.Optional;

public class BasicMap extends BasicMapTemplate {
    public BasicMap(int[][] baseData) {
        super(baseData);
    }

    @Override
    public void initVertexArray(int[][] baseData) {

    }

    @Override
    public void putValuesToBasicMap(int[][] valueArray) {

    }

    @Override
    public Optional<BasicVertex> getBasicVertex(Position2D pos) {
        return Optional.empty();
    }

    @Override
    public int countBasicVerticesWithValue(int vertexValue) {
        return 0;
    }

    @Override
    public int countBasicBuildings() {
        return 0;
    }

    @Override
    public int countBasicStreets() {
        return 0;
    }

    @Override
    public int countBasicGreens() {
        return 0;
    }

    @Override
    public boolean isBasicPathOverStreets(ArrayList<BasicVertex> vertexList) {
        return false;
    }

    @Override
    public boolean isBasicPathByValue(int value) {
        return false;
    }

    @Override
    public boolean hasValidBuildingPlacements() {
        return false;
    }

    @Override
    public boolean isValidBasicMap() {
        return false;
    }
}
