package model.abstractClasses;

import model.BasicMap;
import model.BasicVertex;
import model.Position2D;

public abstract class BasicVertexTemplate {

	private BasicMap containingMap;
	private Position2D position;
	private int value;

	public BasicVertexTemplate(int row, int column, int value) {
		this.value = value;
		initPosition(row, column);
	}

	public abstract void initPosition(int row, int column);

	public abstract boolean isOnBound();

	public abstract boolean isOnCorner();

	public abstract int getBasicManhattanDistance(BasicVertex v);

	public abstract int getBasicDistance(BasicVertex v, int connectValue);

	public abstract boolean isBasicStreetConnectedTo(BasicVertex v);

	public BasicMap getContainingMap() {
		return containingMap;
	}

	public void setContainingMap(BasicMap containingMap) {
		this.containingMap = containingMap;
	}

	public Position2D getPosition() {
		return position;
	}

	public void setPosition(Position2D position) {
		this.position = position;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	

}
