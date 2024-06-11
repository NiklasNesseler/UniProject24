package model.sites;

import model.BasicBuilding;

public class PoliceStation extends BasicBuilding {

    /**
     * Erstellt ein neues BasicBuilding-Objekt mit spezifizierten Positionskoordinaten, einem Wert und einer Höhe.
     * Dieser Konstruktor initialisiert die Position und den Wert des Gebäudes über den Superklassenkonstruktor
     * und setzt die Höhe des Gebäudes.
     *
     * @param row    Die Zeilenposition des Gebäudes in der Karte.
     * @param column Die Spaltenposition des Gebäudes in der Karte.
     * @param value  Der Wert des Gebäudes, der für verschiedene Zwecke verwendet werden kann.
     * @param height Die Höhe des Gebäudes in Stockwerken.
     */
    public PoliceStation(int row, int column, int value, int height) {
        super(row, column, value, height);
    }
}
