package controller;

import controller.abstractClasses.BasicControllerTemplate;
import model.BasicMap;

public class BasicController extends BasicControllerTemplate {

    /**
     * Konstruiert einen BasicController, der die BasicMap mit den angegebenen Basis- und Wertedaten initialisiert.
     * Dieser Konstruktor ruft die initCompleteBasicMap-Methode auf, um die Karte vollständig zu initialisieren.
     *
     * @param baseData Ein zweidimensionales Array von Integern, das die Basisdaten für die Karteninitialisierung enthält.
     *                 Jeder Integer entspricht einem spezifischen Typ von BasicVertex oder einer abgeleiteten Klasse.
     * @param valueData Ein zweidimensionales Array von Integern, das die Wertedaten für jedes Vertex in der Karte enthält.
     */

    public BasicController(int[][] baseData, int[][] valueData) {
        super(baseData, valueData);
    }

    /**
     * Initialisiert die BasicMap vollständig, indem sie zunächst eine neue BasicMap mit den Basisdaten erstellt,
     * dann die Vertex-Werte mit den Wertedaten setzt und schließlich das zugehörige Map-Objekt jedem Vertex zuweist.
     * Diese Methode stellt sicher, dass alle Komponenten der Map korrekt initialisiert und verknüpft sind.
     *
     * @param baseData Die Grunddaten für die Map, die zur Initialisierung der Vertex-Typen verwendet werden.
     * @param valueData Die Wertedaten, die jedem Vertex in der Map zugewiesen werden.
     */

    @Override
    public void initCompleteBasicMap(int[][] baseData, int[][] valueData) {
        BasicMap basicMap = new BasicMap(baseData);
        this.setCompleteBasicMap(basicMap);
        basicMap.putValuesToBasicMap(valueData);
        for (model.BasicVertex[] row : basicMap.getVertexArray()) {
            for (model.BasicVertex vertex: row) {
                if (vertex != null) {
                    vertex.setContainingMap(basicMap);
                }

            }
        }

    }
}
