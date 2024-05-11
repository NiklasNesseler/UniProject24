package controller;

import model.*;

public class PP24_main {

	public static void main(String[] args) {
		//TODO: Insert class implementations

		int[][] baseData = {
				{2, 2, 2, 2},
				{3, 3, 2, 4},
				{1, 1, 2, 3}
		};

		int[][] valueData = {
				{10, 10, 10, 40},
				{15, 25, 10, 45},
				{5, 15, 10, 35}
		};



			BasicController controller = new BasicController(baseData, valueData);

			BasicMap map = controller.getCompleteBasicMap();

			// Test getBasicDistance
			BasicVertex startVertex = map.getVertexArray()[0][0];  // Ein Beispielknoten
			BasicVertex endVertex = map.getVertexArray()[2][2];    // Ein anderer Beispielknoten
			int connectValue = 10; // Verbindungswert, z.B. basierend auf einem spezifischen Wert in valueData
			int distance = startVertex.getBasicDistance(endVertex, connectValue);
			System.out.println("Die Entfernung zwischen den gewählten Knoten beträgt: " + distance);

			// Test isBasicStreetConnectedTo
			BasicVertex street1 = map.getVertexArray()[0][0]; // Angenommen dies ist ein BasicStreet
			BasicVertex street2 = map.getVertexArray()[0][3]; // Ein anderer BasicStreet Knoten
			boolean isConnected = street1.isBasicStreetConnectedTo(street2);
			System.out.println("Sind die Straßen miteinander verbunden? " + isConnected);

//			// Beispiel, um die gesetzten Werte zu überprüfen
//			System.out.println("Wert an Position (0,1): " + map.getVertexArray()[0][1].getValue());
//
//			Position2D pos = new Position2D(1, 0); // Position eines BasicBuilding in baseData
//			if (map.getBasicVertex(pos).isPresent() && map.getBasicVertex(pos).get() instanceof BasicBuilding) {
//				System.out.println("An der Position (" + pos.getRow() + ", " + pos.getColumn() + ") befindet sich ein Gebäude.");
//			}
//
//			// Annahme, dass die Map 3x4 groß ist
//			BasicVertex cornerVertex = new BasicVertex(0, 0, 10); // Ecke oben links
//			cornerVertex.setContainingMap(map);
//			System.out.println("Ist der Vertex an der Ecke? " + cornerVertex.isOnCorner()); // Erwartet: true
//			System.out.println("Ist der Vertex am Rand? " + cornerVertex.isOnBound()); // Erwartet: true
//
//			BasicVertex middleVertex = new BasicVertex(1, 1, 10); // Nicht an einer Ecke oder Kante
//			middleVertex.setContainingMap(map);
//			System.out.println("Ist der Vertex an der Ecke? " + middleVertex.isOnCorner()); // Erwartet: false
//			System.out.println("Ist der Vertex am Rand? " + middleVertex.isOnBound()); // Erwartet: false
//
//			BasicVertex vertexA = new BasicVertex(0, 0, 10);
//			BasicVertex vertexB = new BasicVertex(2, 2, 10);
//			vertexA.setContainingMap(map);
//			vertexB.setContainingMap(map);
//			System.out.println("Manhattan-Distanz zwischen (0,0) und (2,2): " + vertexA.getBasicManhattanDistance(vertexB)); // Erwartet: 4
//
//
//			BasicVertex streetA = new BasicStreet(0, 0, 10, 60);
//			BasicVertex streetB = new BasicStreet(0, 1, 10, 60);
//			BasicVertex streetC = new BasicStreet(2, 2, 10, 60); // Nicht verbunden
//			streetA.setContainingMap(map);
//			streetB.setContainingMap(map);
//			streetC.setContainingMap(map);
//			System.out.println("Sind StreetA und StreetB verbunden? " + streetA.isBasicStreetConnectedTo(streetB)); // Erwartet: true
//			System.out.println("Sind StreetA und StreetC verbunden? " + streetA.isBasicStreetConnectedTo(streetC)); // Erwartet: false
//
//			System.out.println("Distanz von StreetA zu StreetC: " + streetA.getBasicDistance(streetB, 10)); // Erwartet: -1, da nicht verbunden
//
//
//			BasicBuilding building = new BasicBuilding(1, 2, 10, 5);
//
//			// Ausgabe der Höhe des Gebäudes
//			System.out.println("Das Gebäude an der Position (" + building.getPosition().getRow() + ", " + building.getPosition().getColumn() + ") hat eine Höhe von: " + building.getHeight() + " Stockwerken.");
//
//			// Setzen einer neuen Höhe und erneutes Ausgeben
//			building.setHeight(8);
//			System.out.println("Die aktualisierte Höhe des Gebäudes ist: " + building.getHeight() + " Stockwerke.");
//
//
//			System.out.println("Anzahl der Straßen: " + map.countBasicStreets());
//			System.out.println("Anzahl der Gebäude: " + map.countBasicBuildings());
//			System.out.println("Anzahl der Grünflächen: " + map.countBasicGreens());
//			System.out.println("Gesamtanzahl des Wertes 25: " + map.countBasicVerticesWithValue(25));
//
//			System.out.println("Ist die Karte valid? " + map.isValidBasicMap());
//
//			if (!map.isValidBasicMap()) {
//				System.out.println("Die Karte ist ungültig. ");
//			}
	}

}
