package model.advanced;

import model.BasicMap;

public class AdvancedMap{
    private AdvancedVertex[][] advancedVertexArray;

    public AdvancedMap(BasicMap basicMap) {
        initRefinement(basicMap);
    }

    void initRefinement(BasicMap basicMap) {
        //TODO: Implement 8.3
    }

    public AdvancedVertex[][] getAdvancedVertexArray() {
        return advancedVertexArray;
    }

    public void setAdvancedVertexArray(AdvancedVertex[][] advancedVertexArray) {
        this.advancedVertexArray = advancedVertexArray;
    }
}
