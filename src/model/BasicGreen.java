package model;

/**
 * Die Klasse BasicGreen repräsentiert eine Grünfläche innerhalb einer BasicMap.
 * Diese Klasse erbt von BasicVertex und nutzt dessen Attribute und Methoden,
 * um die Position und den Wert der Grünfläche zu definieren und zu verwalten.
 *
 * @author Niklas Nesseler
 */

public class BasicGreen extends BasicVertex{
    /**
     * Konstruiert ein neues BasicGreen-Objekt mit einer spezifischen Position und einem Wert.
     * Dieser Konstruktor initialisiert die Grünfläche basierend auf den angegebenen Parametern.
     * Durch die Vererbung von BasicVertex wird sichergestellt, dass die Grünfläche
     * korrekt innerhalb der übergeordneten Kartenstruktur positioniert ist.
     *
     * @param row Die Zeilenposition der Grünfläche auf der BasicMap.
     * @param column Die Spaltenposition der Grünfläche auf der BasicMap.
     * @param value Ein Wert, der verschiedene Eigenschaften oder Bewertungen der Grünfläche darstellen kann.
     */

    public BasicGreen(int row, int column, int value) {
        super(row, column, value);
    }
}
