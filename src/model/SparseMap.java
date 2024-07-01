package model;

import model.abstractClasses.DensityChecker;
import model.sites.Hospital;
import model.sites.PoliceStation;
import model.sites.Site;

import java.util.*;



/**
 * The SparseMap class extends BasicMap and implements DensityChecker.
 * It represents a sparse map and provides methods to manipulate and query the map.
 */
public class SparseMap extends BasicMap implements DensityChecker {
    /** The sparse vertex array representing the map. */
    private BasicVertex[][] sparseVertexArray;

    /**
     * Constructs a SparseMap with the specified base data.
     *
     * @param baseData the base data for initializing the map
     */
    public SparseMap(int[][] baseData) {
        super(baseData);
        initSparseVertexArray();
    }

    /**
     * Initializes the sparse vertex array with the vertex array from the superclass.
     */
    private void initSparseVertexArray() {
        sparseVertexArray = getVertexArray();
    }

    /**
     * @return the sparse vertex array
     */
    public BasicVertex[][] getSparseVertexArray() {
        return sparseVertexArray;
    }

    /**
     * Sets the sparse vertex array.
     * @param sparseVertexArray the new sparse vertex array
     */
    public void setSparseVertexArray(BasicVertex[][] sparseVertexArray) {
        this.sparseVertexArray = sparseVertexArray;
    }

    /**
     * Replaces buildings at the specified positions with hospitals
     *
     * @param hospitals the positions where hospitals should be placed
     */
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

    /**
     * Replaces buildings at the specified positions with police stations
     *
     * @param policeStations the positions where police stations should be placed
     */
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


    /**
     * Computes all squares in the sparse vertex array and returns them in a sorted TreeSet
     *
     * @return a sorted TreeSet of squares
     */
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



    /**
     * Checks if the map is connected by streets.
     *
     * @return true if the map is connected by streets, false otherwise
     */
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


    /**
     * Helper method that checks if a valid square can be formed with the specified anchor vertex.
     *
     * @param anchor the anchor vertex
     * @param i the row index
     * @param j the column index
     * @return true if a valid square can be formed, false otherwise
     */
    private boolean isValidSquare(BasicVertex anchor, int i, int j) {
        return sparseVertexArray[i][j + 1] != null &&
                sparseVertexArray[i + 1][j + 1] != null &&
                sparseVertexArray[i + 1][j] != null;
    }


    /**
     * Counts the number of common vertices between two lists of vertices.
     *
     * @param x the first list of vertices
     * @param y the second list of vertices
     * @return the number of common vertices
     */
    public int countCommonVertices(ArrayList<BasicVertex> x, ArrayList<BasicVertex> y) {
        Set<BasicVertex> setX = new HashSet<>(x);
        setX.retainAll(y);
        return setX.size();
    }

    /**
     * Checks if subtrip is a subpath of trip.
     *
     * @param trip the main trip
     * @param subtrip the subtrip to check
     * @return true if subtrip is a subpath of trip, false otherwise
     */
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


    /**
     * Checks if the vertices with the specified value are connected.
     *
     * @param connectValue the value to check for connectivity
     * @return true if the vertices with the specified value are connected, false otherwise
     */
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


    /**
     * Checks if the specified path is a crucial path.
     *
     * @param vertexList the list of vertices representing the path
     * @return true if the path is crucial, false otherwise
     */
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


    /**
     * Checks if the map represents a closed world.
     *
     * @return true if the map is a closed world, false otherwise
     */
    public boolean isClosedWorld() {
        if (!isBasicStreetConnectedMap()) {
        return false; }

        for (BasicVertex[] basicVertices : sparseVertexArray) {
            if (basicVertices[0] instanceof BasicStreet
                    && ((BasicStreet) basicVertices[0]).isBasicDeadEnd()) {
                return false;
            }
            if (basicVertices[basicVertices.length - 1] instanceof BasicStreet
                    && ((BasicStreet) basicVertices[basicVertices.length - 1]).isBasicDeadEnd()) {
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


    /**
     * Checks if the specified list of vertices forms a circle.
     *
     * @param vertexList the list of vertices
     * @return true if the list forms a circle, false otherwise
     */
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


    /**
     * Performs a depth first search to check if the list of vertices forms a circle.
     *
     * @param vertexList the list of vertices
     * @param i the current index in the list
     * @param visited the set of visited vertices
     * @param start the starting vertex
     * @return true if the list forms a circle, false otherwise
     */
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


    /**
     * Checks if all streets in the map form a circle.
     *
     * @return true if all streets form a circle, false otherwise
     */
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


    /**
     * Finds a path that includes all streets in the map.
     *
     * @param streets the list of streets
     * @return the list of vertices representing the path
     */
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

    /**
     * Performs a depth first search to find a path that includes all streets.
     *
     * @param current the current vertex
     * @param start the starting vertex
     * @param visited the set of visited vertices
     * @param path the path being constructed
     * @param recursionStack the recursion stack
     * @return true if a path is found, false otherwise
     */
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


    /**
     * Checks if all streets in the map are connected.
     *
     * @return true if all streets are connected, false otherwise
     */
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

    /**
     * Checks if a street vertex is connected to another street.
     *
     * @param street the street vertex
     * @return true if the street is connected to another street, false otherwise
     */
    private boolean isConnectedToAnotherStreet(BasicVertex street) {
        List<BasicVertex> neighbours = street.getNeighbours();

        for (BasicVertex neighbour : neighbours) {
            if (neighbour instanceof BasicStreet) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the specified vertex list forms a tour with the given stops.
     *
     * @param vertexList the list of vertices
     * @param stops the list of stops
     * @return true if the vertex list forms a tour, false otherwise
     */
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
        int stopIndex = 0;
        for (int i = 0; i < vertexList.size() - 1; i++) {
            BasicVertex vertex = vertexList.get(i);
            if (stopSet.contains(vertex)) {
                if (visitedStops.contains(vertex)) {
                    return false;
                }
                visitedStops.add(vertex);
                if (stopIndex < stops.size() && !vertex.equals(stops.get(stopIndex))) {
                    return false;
                }
                stopIndex++;
                if (stopIndex > stops.size()) {

                    return false;
                }
            }
        }

        return stopIndex == stops.size();
    }



    /**
     * Replaces a vertex with a BasicGreen vertex.
     *
     * @param vertex the vertex to replace
     */
    private void replaceWithBasicGreen(BasicVertex vertex) {
        int row = vertex.getPosition().getRow();
        int column = vertex.getPosition().getColumn();
        BasicGreen basicGreen = new BasicGreen(row, column, vertex.getValue());
        sparseVertexArray[row][column] = basicGreen;
    }

    /**
     * Resets a position to its original vertex.
     *
     * @param position2D the position
     * @param originalVertex the original vertex
     */
    private void resetBackToBasicStreet(Position2D position2D, BasicVertex originalVertex) {
        sparseVertexArray[position2D.getRow()][position2D.getColumn()] = originalVertex;
    }


    /**
     * Replaces a vertex at a given position with a new vertex.
     *
     * @param position the position of the vertex
     * @param newVertex the new vertex to replace the old vertex
     */
    public void replaceVertex(Position2D position, BasicVertex newVertex) {
        sparseVertexArray[position.getRow()][position.getColumn()] = newVertex;
    }

    public boolean areStreetConnected(BasicVertex a, BasicVertex b) {
        if (a == b) return true;

        Queue<BasicVertex> q = new LinkedList<>();
        Set<BasicVertex> visited = new HashSet<>();

        q.offer(a);
        visited.add(a);

        while (!q.isEmpty()){
            BasicVertex current = q.poll();
            if (current == b) return true;

            for (BasicVertex neighbour : current.getNeighbours()) {
                if (!visited.contains(neighbour) && neighbour instanceof BasicStreet || neighbour == b) {
                    q.offer(neighbour);
                    visited.add(neighbour);
                }
            }
        }
        return false;
    }


}
