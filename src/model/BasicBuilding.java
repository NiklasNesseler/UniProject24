package model;

public class BasicBuilding extends BasicVertex{
    /**
     * Die Klasse BasicBuilding repräsentiert ein Gebäude innerhalb einer BasicMap. Dieses Gebäude
     * steht als Knoten im graphbasierten Kontext und erbt grundlegende Eigenschaften von BasicVertex.
     * BasicBuilding speichert zusätzliche Informationen für Gebäude, wie die Höhe.
     *
     */
    private int height;
    /**
     * Erstellt ein neues BasicBuilding-Objekt mit spezifizierten Positionskoordinaten, einem Wert und einer Höhe.
     * Dieser Konstruktor initialisiert die Position und den Wert des Gebäudes über den Superklassenkonstruktor
     * und setzt die Höhe des Gebäudes.
     *
     * @param row Die Zeilenposition des Gebäudes in der Karte.
     * @param column Die Spaltenposition des Gebäudes in der Karte.
     * @param value Der Wert des Gebäudes, der für verschiedene Zwecke verwendet werden kann.
     * @param height Die Höhe des Gebäudes in Stockwerken.
     */

    public BasicBuilding(int row, int column, int value, int height) {
        super(row, column, value);
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
