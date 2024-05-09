package model;

import model.abstractClasses.BasicMapTemplate;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Die Klasse BasicMap stellt eine Kartenstruktur dar, in der verschiedene Arten von Knoten
 * wie Gebäude, Straßen und Grünflächen organisiert sind. Diese Klasse erbt von BasicMapTemplate
 * und implementiert spezifische Funktionen für das Initialisieren und Verwalten der Knoten in der Karte.
 *
 * Die Klasse verwendet ein zweidimensionales Array von BasicVertex-Objekten, das als vertexArray bezeichnet wird.
 * Jeder Knoten in diesem Array kann ein BasicVertex sein oder von BasicVertex abgeleitete Klassen wie BasicStreet,
 * BasicBuilding oder BasicGreen. Diese Klassifizierung basiert auf den Daten, die im Konstruktor übergeben werden.
 *
 * <p>Methodenübersicht:</p>
 * <ul>
 *   <li>initVertexArray - Initialisiert das vertexArray basierend auf den übergebenen Basiskonfigurationsdaten.</li>
 *   <li>putValuesToBasicMap - Füllt das vertexArray mit spezifischen Werten für jeden Knoten.</li>
 *   <li>getBasicVertex - Gibt ein Optional-Objekt zurück, das ein BasicVertex an einer spezifischen Position enthält.</li>
 *   <li>countBasicVerticesWithValue - Zählt Knoten eines bestimmten Werts.</li>
 *   <li>countBasicBuildings, countBasicStreets, countBasicGreens - Zählen spezifische Typen von Knoten in der Karte.</li>
 *   <li>isBasicPathOverStreets - Überprüft, ob eine Liste von Knoten einen gültigen Pfad über Straßen bildet.</li>
 *   <li>isBasicPathByValue - Bestimmt, ob Knoten mit einem bestimmten Wert so angeordnet werden können, dass sie einen Pfad bilden.</li>
 *   <li>hasValidBuildingPlacements - Überprüft, ob alle Gebäude gemäß den Regeln gültig platziert sind.</li>
 *   <li>isValidBasicMap - Überprüft, ob die Karte insgesamt gültige Bedingungen erfüllt.</li>
 * </ul>
 *
 *
 * @author Niklas Nesseler
 */

public class BasicMap extends BasicMapTemplate {
    public BasicMap(int[][] baseData) {
        super(baseData);
        initVertexArray(baseData);

    }

    @Override
    public void initVertexArray(int[][] baseData) {
        //8.3 a)
        BasicVertex[][] vertexArray;
        vertexArray = new BasicVertex[baseData.length][baseData[0].length];
        for (int i = 0; i < baseData.length; i++) {
            for (int j = 0; j < baseData[i].length; j++) {
                switch (baseData[i][j]) {
                    case 1:
                        vertexArray[i][j] = new BasicVertex(i, j, -1);
                        break;
                    case 2:
                        vertexArray[i][j] = new BasicStreet(i, j, -1, -1);
                        break;
                    case 3:
                        vertexArray[i][j] = new BasicBuilding(i, j, -1, -1);
                        break;
                    default:
                        vertexArray[i][j] = new BasicGreen(i, j, -1);
                        break;
                }

            }
        }

        super.setVertexArray(vertexArray);

    }

    @Override
    public void putValuesToBasicMap(int[][] valueArray) {
        //give the values to the vertex array
        for (int row = 0; row < getVertexArray().length; row++) {
            for (int column = 0; column < getVertexArray()[0].length; column++) {
                getVertexArray()[row][column].setValue(valueArray[row][column]);
            }
        }

    }

    @Override
    public Optional<BasicVertex> getBasicVertex(Position2D pos) {
        //return Optional if the position exists
        if (pos.getRow() >= 0 && pos.getRow() < getVertexArray().length && pos.getColumn() >= 0 && pos.getColumn() < getVertexArray()[0].length) {
            return Optional.of(getVertexArray()[pos.getRow()][pos.getColumn()]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int countBasicVerticesWithValue(int vertexValue) {
        //count the number of BasicVertices with vertexValue as their value
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex.getValue() == vertexValue) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countBasicBuildings() {
        //count the number of BasicBuildings
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicBuilding) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countBasicStreets() {
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countBasicGreens() {
        int count = 0;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicGreen) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public boolean isBasicPathOverStreets(ArrayList<BasicVertex> vertexList) {
        //returns true if the whole path is a street except for the last one
        if (vertexList == null || vertexList.isEmpty()) {
            return false;
        }
        for (int i = 0; i < vertexList.size() - 1; i++) {
            if (vertexList.get(i).isBasicStreetConnectedTo(vertexList.get(i + 1))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBasicPathByValue(int value) {
        //returns true if it is possible to arrange vertices with the given value in a single path
        //check if there is a path from one vertex with the given value to another with the same value
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex.getValue() == value) {
                    for (BasicVertex otherVertex : row) {
                        if (otherVertex.getValue() == value && vertex.getBasicManhattanDistance(otherVertex) == 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasValidBuildingPlacements() {
        //returns true if there is no BasicBuilding object in the vertex array or if every BasicBuilding has at least one adjacent BasicStreet
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (!(vertex instanceof BasicBuilding) || vertex.isBasicStreetConnectedTo(vertex)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isValidBasicMap() {
        //returns true if all vertices are not null, all vertices have a value of at least 1, hasValidBuildingPlacements() returns true, and every street is connected
        boolean isValid = true;
        for (BasicVertex[] row : getVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex == null || vertex.getValue() < 1) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid)
                break;
        }

        if (isValid && hasValidBuildingPlacements()) {
            for (BasicVertex[] row : getVertexArray()) {
                for (BasicVertex vertex : row) {
                    if (!vertex.isBasicStreetConnectedTo(vertex)) {
                        isValid = false;
                        break;
                    }
                }
                if (!isValid)
                    break;
            }

        }

        return isValid;
    }
}
