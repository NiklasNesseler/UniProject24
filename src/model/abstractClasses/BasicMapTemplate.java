package model.abstractClasses;

import java.util.ArrayList;
import java.util.Optional;

import model.BasicVertex;
import model.Position2D;

public abstract class BasicMapTemplate {
	
	private BasicVertex[][] vertexArray;
	
	public BasicMapTemplate(int[][] baseData) {
		initVertexArray(baseData);
	}
	
	public abstract void initVertexArray(int[][] baseData);
	
	public abstract void putValuesToBasicMap(int[][] valueArray);
	
	public abstract Optional<BasicVertex> getBasicVertex(Position2D pos);
	
	public abstract int countBasicVerticesWithValue(int vertexValue);
	
	public abstract int countBasicBuildings();
	
	public abstract int countBasicStreets();
	
	public abstract int countBasicGreens();
	
	public abstract boolean isBasicPathOverStreets(ArrayList<BasicVertex> vertexList);
	
	public abstract boolean isBasicPathByValue(int value);
	
	public abstract boolean hasValidBuildingPlacements();
	
	public abstract boolean isValidBasicMap();
	
	public BasicVertex[][] getVertexArray() {
		return vertexArray;
	}

	public void setVertexArray(BasicVertex[][] vertexArray) {
		this.vertexArray = vertexArray;
	}

}
