package controller;

import controller.abstractClasses.BasicControllerTemplate;
import model.BasicMap;

public class BasicController extends BasicControllerTemplate {

    public BasicController(int[][] baseData, int[][] valueData) {
        super(baseData, valueData);
    }

    @Override
    public void initCompleteBasicMap(int[][] baseData, int[][] valueData) {
        //9.3 a)
        BasicMap basicMap = new BasicMap(baseData);
        this.setCompleteBasicMap(basicMap);
        //9.3 b)
        basicMap.putValuesToBasicMap(valueData);
        //9.3 c)
        for (model.BasicVertex[] row : basicMap.getVertexArray()) {
            for (model.BasicVertex vertex: row) {
                if (vertex != null) {
                    vertex.setContainingMap(basicMap);
                }

            }
        }

    }
}
