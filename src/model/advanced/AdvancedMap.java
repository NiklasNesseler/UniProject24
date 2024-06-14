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

                    AdvancedStreet.StreetTypes newStreetType = switch (x) {
                        case 1 -> AdvancedStreet.StreetTypes.CROSSING;
                        case 2 -> AdvancedStreet.StreetTypes.TJUNCTION;
                        case 3 -> AdvancedStreet.StreetTypes.LINE;
                        case 4 -> AdvancedStreet.StreetTypes.CURVE;
                        case 5 -> AdvancedStreet.StreetTypes.NEEDLE;
                        default -> throw new IllegalArgumentException("Unknown street type: " + x);
                    };

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

    void isAdvancedCircle(ArrayList<AdvancedStreet> streetList) {
        //ist erster = letzter
        // ist Path für alle bis auf letzten?
        // verknüpfung zwischen vorletzten und letzten street
    }

    void hasSkeleton() {
        //Folie 29 fragen
        //kein Dummie
        //erstmal typ checken
        //Typen kompatibel zueinander für fall 3 (Hilfsmethode)

    }




    public AdvancedVertex[][] getAdvancedVertexArray() {
        return advancedVertexArray;
    }

    public void setAdvancedVertexArray(AdvancedVertex[][] advancedVertexArray) {
        this.advancedVertexArray = advancedVertexArray;
    }
}
