package model.abstractClasses;

import model.BasicVertex;
import model.Square;

import java.util.ArrayList;
import java.util.TreeSet;

public interface DensityChecker {



    boolean isSparse();
    boolean isBasicStreetConnectedMap();
    int countCommonVertices(ArrayList<BasicVertex> x, ArrayList<BasicVertex> y);
    boolean isSubtrip(ArrayList<BasicVertex> trip, ArrayList<BasicVertex> subtrip);
    boolean isConnectedByValue(int connectValue);
    boolean isCrucialPath(ArrayList<BasicVertex> vertexList);
    boolean isClosedWorld();
    boolean isCircle(ArrayList<BasicVertex> vertexList);
    boolean isCircle();
    boolean isTour(ArrayList<BasicVertex> vertexList, ArrayList<BasicVertex> stops);

}
