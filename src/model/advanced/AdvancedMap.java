package model.advanced;

import model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    public void buildAdvancedStreets(int[][] streetCodes) {
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

    boolean isAdvancedPath(ArrayList<AdvancedStreet> streetList) {
        if (streetList == null || streetList.isEmpty() || streetList.size() < 3) {
            return false;
        }

        AdvancedStreet first = streetList.getFirst();
        AdvancedStreet last = streetList.getLast();

        Set<AdvancedStreet> visited = new HashSet<>();
        visited.add(first);

        for (int i = 0; i < streetList.size() - 1; i++) {
            AdvancedStreet current = streetList.get(i);
            AdvancedStreet next = streetList.get(i + 1);
            if (visited.contains(next)) {
                return false;
            }

            if (!current.isAdvancedStreetConnectedTo(next, visited)) {
                return false;
            }
            visited.add(next);
        }
        return true;
    }




    public AdvancedVertex[][] getAdvancedVertexArray() {
        return advancedVertexArray;
    }

    public void setAdvancedVertexArray(AdvancedVertex[][] advancedVertexArray) {
        this.advancedVertexArray = advancedVertexArray;
    }
}
