package model;

//TODO: Nichts! A3 erfüllt

import model.abstractClasses.Position2DTemplate;
/**
 * Die Klasse Position2D repräsentiert eine Position in einer zweidimensionalen Karte.
 * Sie erweitert die abstrakte Klasse Position2DTemplate und erbt deren Attribute
 * und Methoden zur Repräsentation von Zeilen- und Spaltenindizes.
 *
 * Diese Klasse wird hauptsächlich verwendet, um eine spezifische Position innerhalb einer
 * Map zu definieren und zu verwalten.
 *
 * @author Niklas Nesseler
 */

public class Position2D extends Position2DTemplate {

    public Position2D(int row, int column) {
        super(row, column);
    }
}
