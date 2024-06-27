package model;

import model.abstractClasses.DensityChecker;
import model.sites.Hospital;
import model.sites.PoliceStation;
import model.sites.Site;

import java.util.*;

//TODO: OFFENE FRAGEN: Muss bei isCrucialPath() der sparseVertexArray zurückersetzt werden?
//TODO: Wie ist isCircle genau definiert? Kann in der VertexList auch ein Punkt 2 mal vorkommen?

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

    public void putHospitals(ArrayList<Position2D> hospitals) {
        for (Position2D position : hospitals) {
            BasicVertex existingBuilding = sparseVertexArray[position.getRow()][position.getColumn()];
            if (existingBuilding instanceof BasicBuilding building) {
                Hospital hospital = new Hospital(position.getRow(), position.getColumn(), building.getValue(), building.getHeight());
                hospital.setContainingMap(this);
                sparseVertexArray[position.getRow()][position.getColumn()] = hospital;
            }
        }

    }

    public void putPoliceStations(ArrayList<Position2D> policeStations) {
        for (Position2D position : policeStations) {
            BasicVertex existingBuilding = sparseVertexArray[position.getRow()][position.getColumn()];
            if (existingBuilding instanceof BasicBuilding building) {
                PoliceStation policeStation = new PoliceStation(position.getRow(), position.getColumn(), building.getValue(), building.getHeight());
                policeStation.setContainingMap(this);
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
                BasicVertex anchor = sparseVertexArray[i][j];
                if (isValidSquare(anchor, i, j)) {
                    Square square = new Square(anchor);
                    if (!square.isSparse()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private boolean isValidSquare(BasicVertex anchor, int i, int j) {
        return sparseVertexArray[i][j + 1] != null &&
                sparseVertexArray[i + 1][j + 1] != null &&
                sparseVertexArray[i + 1][j] != null;
    }

    public int countCommonVertices(ArrayList<BasicVertex> x, ArrayList<BasicVertex> y) {
        Set<BasicVertex> setX = new HashSet<>(x);
        setX.retainAll(y);
        return setX.size();
    }

    public boolean isSubtrip(ArrayList<BasicVertex> trip, ArrayList<BasicVertex> subtrip) {
        if (subtrip.isEmpty()) return true;
        if (subtrip.size() > trip.size()) return false;

        for (int i = 0; i <= trip.size() - subtrip.size(); i++) {
            boolean match = true;
            for (int j = 0; j < subtrip.size(); j++) {
                if (!trip.get(i + j).equals(subtrip.get(j))) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }

        return false;
    }


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
        queue.add(vertexWithValue.getFirst());
        visited.add(vertexWithValue.getFirst());

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


    public boolean isCrucialPath(ArrayList<BasicVertex> vertexList) {
        if (vertexList.size() < 2) {
            return false;
        }

        if (!isBasicPathOverStreets(vertexList)) {
            return false;
        }

        BasicVertex s = vertexList.getFirst();
        BasicVertex t = vertexList.getLast();

        List<BasicVertex> streetVertices = new ArrayList<>(vertexList.subList(1, vertexList.size() - 1));
        Map<Position2D, BasicVertex> originalMap = new HashMap<>();
        for (BasicVertex vertex : streetVertices) {
            originalMap.put(vertex.getPosition(), vertex);
            replaceWithBasicGreen(vertex);
        }
        boolean result = !s.isBasicStreetConnectedTo(t);

        for (Map.Entry<Position2D, BasicVertex> entry : originalMap.entrySet()) {
            resetBackToBasicStreet(entry.getKey(), entry.getValue());
        }


        return result;
    }


    public boolean isClosedWorld() {
        if (!isBasicStreetConnectedMap()) {
        return false; }

        for (int i = 0; i < sparseVertexArray.length; i++) {
            if (sparseVertexArray[i][0] instanceof BasicStreet
                    && ((BasicStreet) sparseVertexArray[i][0]).isBasicDeadEnd()) {
                return false;
            }
            if (sparseVertexArray[i][sparseVertexArray[i].length - 1] instanceof BasicStreet
                    && ((BasicStreet) sparseVertexArray[i][sparseVertexArray[i].length - 1]).isBasicDeadEnd()) {
                return false;
            }
        }
        for (int j = 0; j < sparseVertexArray[0].length - 1; j++) {
            if (sparseVertexArray[0][j] instanceof BasicStreet
                    && ((BasicStreet) sparseVertexArray[0][j]).isBasicDeadEnd()) {
                return false;
            }
            if (sparseVertexArray[sparseVertexArray.length - 1][j] instanceof BasicStreet
                    && ((BasicStreet) sparseVertexArray[sparseVertexArray.length - 1][j]).isBasicDeadEnd()) {
                return false;
            }
        }

        return true;
    }


    public boolean isCircle(ArrayList<BasicVertex> vertexList) {
        if (vertexList == null || vertexList.size() < 4) {

            return false;
        }

        BasicVertex first = vertexList.getFirst();
        BasicVertex last = vertexList.getLast();

        if (!first.equals(last)) {
            return false;
        }

        Set<BasicVertex> visited = new HashSet<>();
        visited.add(first);
        return dfs(vertexList, 0, visited, first);

    }

    private boolean dfs(ArrayList<BasicVertex> vertexList, int i, Set<BasicVertex> visited, BasicVertex start) {
        if (i == vertexList.size() - 1) {
            return true;
        }

        BasicVertex current = vertexList.get(i);
        BasicVertex next = vertexList.get(i + 1);


        if (!current.getNeighbours().contains(next) || (visited.contains(next) && !next.equals(start))) {
            return false;
        }


        visited.add(current);
        return dfs(vertexList, i + 1, visited, start);
    }


    public boolean isCircle() {
        if (!areAllStreetsConnected()) {

            return false;
        }

        List<BasicStreet> streets = new ArrayList<>();

        for (BasicVertex[] basicVertices : sparseVertexArray) {
            for (BasicVertex basicVertex : basicVertices) {
                if (basicVertex instanceof BasicStreet) {
                    streets.add((BasicStreet) basicVertex);
                }
            }
        }
        if (streets.isEmpty() || streets.size() < 4) {

            return false;
        }

        List<BasicVertex> vertexList = findPath(streets);


        if (vertexList.size() != streets.size() + 1 || !vertexList.getFirst().equals(vertexList.getLast())) {

            return false;
        }


        return isCircle(new ArrayList<>(vertexList));
    }

    private List<BasicVertex> findPath(List<BasicStreet> streets) {
        if (streets.isEmpty()) {
            return Collections.emptyList();
        }
        List<BasicVertex> path = new ArrayList<>();
        Set<BasicVertex> visited = new HashSet<>();

        BasicVertex start = streets.getFirst();
        if (dfs2(start, start, visited, path, new HashSet<>())) {
            path.add(start);
        }

        return path;
    }

    private boolean dfs2(BasicVertex current, BasicVertex start, Set<BasicVertex> visited, List<BasicVertex> path, Set<BasicVertex> recursionStack) {
        visited.add(current);
        path.add(current);
        recursionStack.add(current);

        for (BasicVertex neighbor : current.getNeighbours()) {
            if (neighbor.equals(start) && recursionStack.size() > 2) {
                return true;
            }
            if (neighbor instanceof BasicStreet && !visited.contains(neighbor)) {
                if (dfs2(neighbor, start, visited, path, recursionStack)) {
                    return true;
                }
            }
        }

        path.removeLast();
        recursionStack.remove(current);
        visited.remove(current);
        return false;
    }


    private boolean areAllStreetsConnected() {
        for (BasicVertex[] row : getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    if (!isConnectedToAnotherStreet(vertex)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isConnectedToAnotherStreet(BasicVertex street) {
        List<BasicVertex> neighbours = street.getNeighbours();

        for (BasicVertex neighbour : neighbours) {
            if (neighbour instanceof BasicStreet) {
                return true;
            }
        }
        return false;
    }


    public boolean isTour(ArrayList<BasicVertex> vertexList, ArrayList<BasicVertex> stops) {
        if (vertexList == null || stops == null || stops.isEmpty() || vertexList.isEmpty()) {
            return false;
        }

        Set<BasicVertex> stopSet = new HashSet<>(stops);
        if (stopSet.size() != stops.size()) {
            return false;
        }

        if (!isCircle(vertexList)) {
            return false;
        }

        for (BasicVertex vertex : vertexList) {
            if (!stopSet.contains(vertex) && !(vertex instanceof BasicStreet)) {
                return false;
            }
        }

        if (!vertexList.getFirst().equals(stops.getFirst())) {
            return false;
        }

        Set<BasicVertex> visitedStops = new HashSet<>();
        for (int i = 1; i < stops.size(); i++) {
            BasicVertex stop = stops.get(i);
            if (!vertexList.contains(stop)) {
                return false;
            }

            visitedStops.add(stop);
        }

        int stopIndex = 1;
        for (int i = 1; i < vertexList.size() && stopIndex < stops.size(); i++) {
            if (vertexList.get(i).equals(stops.get(stopIndex))) {
                stopIndex++;
            }
        }
        return stopIndex == stops.size();
    }


    private void replaceWithBasicGreen(BasicVertex vertex) {
        int row = vertex.getPosition().getRow();
        int column = vertex.getPosition().getColumn();
        BasicGreen basicGreen = new BasicGreen(row, column, vertex.getValue());
        sparseVertexArray[row][column] = basicGreen;
    }

    private void resetBackToBasicStreet(Position2D position2D, BasicVertex originalVertex) {
        sparseVertexArray[position2D.getRow()][position2D.getColumn()] = originalVertex;
    }


    public void replaceVertex(Position2D position, BasicVertex newVertex) {
        sparseVertexArray[position.getRow()][position.getColumn()] = newVertex;
    }


}
