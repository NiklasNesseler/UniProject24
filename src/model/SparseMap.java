package model;

import model.abstractClasses.DensityChecker;
import model.sites.Hospital;
import model.sites.PoliceStation;

import java.util.*;

public class SparseMap extends BasicMap implements DensityChecker {
    private BasicVertex[][] sparseVertexArray;

    public SparseMap(int[][] baseData) {
        super(baseData);
        initSparseVertexArray();
    }

    private void initSparseVertexArray() {
        sparseVertexArray = getVertexArray();
    }

    public BasicVertex[][] getSparseVertexArray() {
        return sparseVertexArray;
    }

    public void setSparseVertexArray(BasicVertex[][] sparseVertexArray) {
        this.sparseVertexArray = sparseVertexArray;
    }

    void putHospitals(ArrayList<Position2D> hospitals) {
        for (Position2D position : hospitals) {
            BasicVertex existingBuilding = sparseVertexArray[position.getRow()][position.getColumn()];
            if (existingBuilding instanceof BasicBuilding building) {
                Hospital hospital = new Hospital(position.getRow(), position.getColumn(), building.getValue(), building.getHeight());
                sparseVertexArray[position.getRow()][position.getColumn()] = hospital;
            }
        }

    }

    void putPoliceStations(ArrayList<Position2D> policeStations) {
        for (Position2D position : policeStations) {
            BasicVertex existingBuilding = sparseVertexArray[position.getRow()][position.getColumn()];
            if (existingBuilding instanceof BasicBuilding building) {
                PoliceStation policeStation = new PoliceStation(position.getRow(), position.getColumn(), building.getValue(), building.getHeight());
                sparseVertexArray[position.getRow()][position.getColumn()] = policeStation;
            }
        }

    }


    public TreeSet<Square> computeAllSquares() {
        TreeSet<Square> squares = new TreeSet<>();
        for (int i = 0; i < sparseVertexArray.length - 1; i++) {
            for (int j = 0; j < sparseVertexArray[i].length - 1; j++) {
                BasicVertex topLeft = sparseVertexArray[i][j];
                if (isValidSquare(topLeft, i, j)) {
                    squares.add(new Square(topLeft));
                }
            }
        }
        return squares;
    }


    @Override
    public boolean isBasicStreetConnectedMap() {
        BasicVertex start = null;

        for (BasicVertex[] row : sparseVertexArray) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    start = vertex;
                    break;
                }
            }
            if (start != null) break;
        }
        if (start == null) return true;

        Set<BasicVertex> visited = new HashSet<>();
        Queue<BasicVertex> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            BasicVertex current = queue.poll();
            for (BasicVertex neighbour : current.getNeighbours()) {
                if (neighbour instanceof BasicStreet && !visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        for (BasicVertex[] row : sparseVertexArray) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet && !visited.contains(vertex)) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public boolean isSparse() {
        for (int i = 0; i < sparseVertexArray.length - 1; i++) {
            for (int j = 0; j < sparseVertexArray[i].length - 1; j++) {
                BasicVertex anchorpoint = sparseVertexArray[i][j];
                if (isValidSquare(anchorpoint, i, j)) {
                    Square square = new Square(anchorpoint);
                    if (!square.isSparse()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private boolean isValidSquare(BasicVertex anchorpoint, int i, int j) {
        return sparseVertexArray[i][j + 1] != null &&
                sparseVertexArray[i + 1][j + 1] != null &&
                sparseVertexArray[i + 1][j] != null;
    }

    @Override
    public int countCommonVertices(ArrayList<BasicVertex> x, ArrayList<BasicVertex> y) {
        Set<BasicVertex> setX = new HashSet<>(x);
        setX.retainAll(y);
        return setX.size();
    }

    @Override
    public boolean isSubtrip(ArrayList<BasicVertex> trip, ArrayList<BasicVertex> subtrip) {
        if (subtrip.isEmpty()) return true;
        if (subtrip.size() > trip.size()) return false;

        for (int i = 0; i <= trip.size() - subtrip.size(); i++) {
            boolean match = true;
            for (int j = 0; j <= subtrip.size(); j++) {
                if (!trip.get(i + j).equals(subtrip.get(j))) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }

        return false;
    }

    @Override
    public boolean isConnectedByValue(int connectValue) {
        List<BasicVertex> vertexWithValue = new ArrayList<>();
        for (BasicVertex[] row : sparseVertexArray) {
            for (BasicVertex vertex : row) {
                if (vertex.getValue() == connectValue) {
                    vertexWithValue.add(vertex);
                }
            }
        }
        if (vertexWithValue.isEmpty()) return true;

        //BFS
        Set<BasicVertex> visited = new HashSet<>();
        Queue<BasicVertex> queue = new LinkedList<>();
        queue.add(vertexWithValue.get(0));
        visited.add(vertexWithValue.get(0));

        while (!queue.isEmpty()) {
            BasicVertex current = queue.poll();
            for (BasicVertex neighbour : current.getNeighbours()) {
                if (neighbour.getValue() == connectValue && !visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }

        return visited.size() == vertexWithValue.size();
    }
}
