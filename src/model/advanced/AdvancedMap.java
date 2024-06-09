package model.advanced;

import model.*;

public class AdvancedMap{
    private AdvancedVertex[][] advancedVertexArray;

    public AdvancedMap(BasicMap basicMap) {
        initRefinement(basicMap);
    }

    void initRefinement(BasicMap basicMap) {
        BasicVertex[][] vertexArray = basicMap.getVertexArray();
        int rows = vertexArray.length;
        int columns = vertexArray[0].length;

        advancedVertexArray = new AdvancedVertex[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                BasicVertex basicVertex = vertexArray[i][j];
                if (basicVertex instanceof BasicStreet) {
                    advancedVertexArray[i][j] = new AdvancedStreet(i, j, basicVertex.getValue(), AdvancedStreet.StreetTypes.DUMMY);
                } else if (basicVertex instanceof BasicGreen) {
                    advancedVertexArray[i][j] = new AdvancedGreen(i, j, basicVertex.getValue());
                } else if (basicVertex instanceof BasicBuilding) {
                    advancedVertexArray[i][j] = new AdvancedBuilding(i, j, basicVertex.getValue());
                }

                advancedVertexArray[i][j].setContainingAdvancedMap(this);
            }
        }
    }

    void buildAdvancedStreets(int[][] streetCodes) {
        for (int i = 0; i < streetCodes.length; i++) {
            for (int j = 0; j < streetCodes[i].length; j++) {
                AdvancedVertex vertex = advancedVertexArray[i][j];
                if (vertex instanceof AdvancedStreet && ((AdvancedStreet) vertex).type == AdvancedStreet.StreetTypes.DUMMY) {
                    int code = streetCodes[i][j];
                    int x = code / 10;
                    int y = code % 10;

                    AdvancedStreet.StreetTypes newStreetType;
                    switch (x) {
                        case 1: newStreetType = AdvancedStreet.StreetTypes.CROSSING;
                        break;
                        case 2: newStreetType = AdvancedStreet.StreetTypes.TJUNCTION;
                        break;
                        case 3: newStreetType = AdvancedStreet.StreetTypes.LINE;
                        break;
                        case 4: newStreetType = AdvancedStreet.StreetTypes.CURVE;
                        break;
                        case 5: newStreetType = AdvancedStreet.StreetTypes.NEEDLE;
                        break;
                        default: throw new IllegalArgumentException("Unknown street type: " + x);
                    }

                    AdvancedStreet street = (AdvancedStreet) vertex;
                    street.type = newStreetType;
                    street.initParts(newStreetType);

                    for (int k = 0; k < y; k++) {
                        street.rotate90();
                    }
                }
            }
        }
    }

    public AdvancedVertex[][] getAdvancedVertexArray() {
        return advancedVertexArray;
    }

    public void setAdvancedVertexArray(AdvancedVertex[][] advancedVertexArray) {
        this.advancedVertexArray = advancedVertexArray;
    }
}
