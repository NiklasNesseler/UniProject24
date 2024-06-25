package controller.advanced;

import controller.BasicController;
import model.advanced.AdvancedMap;

public class AdvancedController {
    AdvancedMap advancedMap;

    public AdvancedController(int[][] baseData, int[][] valueData, int[][] streetCodes) {
        initAdvancedMap(baseData, valueData, streetCodes);
    }

    void initAdvancedMap(int[][] baseData, int[][] valueData, int[][] streetCodes) {
        BasicController basicController = new BasicController(baseData, valueData);
        this.advancedMap = new AdvancedMap(basicController.getCompleteBasicMap());
        this.advancedMap.buildAdvancedStreets(streetCodes);
    }

    public AdvancedMap getAdvancedMap() {
        return advancedMap;
    }

    public void setAdvancedMap(AdvancedMap advancedMap) {
        this.advancedMap = advancedMap;
    }
}
