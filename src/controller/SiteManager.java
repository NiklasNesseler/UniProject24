package controller;

import model.*;
import model.sites.Hospital;
import model.sites.PoliceStation;
import model.sites.Site;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Class to manage Construction Sites in the SparseMap
 */
public class SiteManager {
    SparseMap siteMap;

    public SiteManager(SparseMap siteMap) {
        this.siteMap = siteMap;
    }

    /**
     * Replaces all vertices in Sitemap which are in positionList with Construction Sites
     *
     * @param positionList the List of Vertices to be replaced
     */
    public void putSites(ArrayList<Position2D> positionList) {
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = siteMap.getBasicVertex(position);
            if (vertex.isPresent()) {
                BasicVertex v = vertex.get();
                Site site = new Site(position.getRow(), position.getColumn(), v.getValue(), 0);
                site.setContainingMap(siteMap);
                siteMap.replaceVertex(position, site);
            }
        }
    }

    /**
     * Checks if replacing Vertices with Sites Disconnects street components
     *
     * @param positionList list of vertices to be replaced
     * @return true if components go up by at least 1 when vertices get replaced
     */
    public boolean isDisconnectedSet(ArrayList<Position2D> positionList) {
        Map<Position2D, BasicVertex> originalVertices = new HashMap<>();
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = siteMap.getBasicVertex(position);
            if (vertex.isPresent()) {
                originalVertices.put(position, vertex.get());
                Site site = new Site(position.getRow(), position.getColumn(), vertex.get().getValue(), 0);
                site.setContainingMap(vertex.get().getContainingMap());
                siteMap.replaceVertex(position, site);
            }
        }


        int initialComponents = countComponents();
        for (Position2D position : positionList) {
            siteMap.replaceVertex(position, originalVertices.get(position));
        }
        int newComponents = countComponents();


        //Fragt mich nicht warum aber das funktioniert
        return newComponents < initialComponents;
    }


    /**
     * Helper method for isDisconnectedSet
     *
     * @return the number of components
     */
    int countComponents() {
        Set<BasicVertex> visited = new HashSet<>();
        int components = 0;

        for (BasicVertex[] row : siteMap.getSparseVertexArray()) {
            for (BasicVertex v : row) {
                if (v instanceof BasicStreet && ((BasicStreet) v).getSpeedLimit() != 0 && !visited.contains(v)) {
                    List<BasicVertex> component = new ArrayList<>();
                    dfs(v, visited, component);
                    if (!component.isEmpty()) {
                        components++;
                    }
                }
            }
        }
        return components;
    }

    /**
     * depth first search to help find components
     *
     * @param v         current vertex
     * @param visited   List of visited vertices
     * @param component List of vertices in current component
     */
    private void dfs(BasicVertex v, Set<BasicVertex> visited, List<BasicVertex> component) {
        visited.add(v);
        component.add(v);

        for (BasicVertex neighbour : v.getNeighbours()) {
            if (neighbour instanceof BasicStreet && ((BasicStreet) neighbour).getSpeedLimit() != 0 && !visited.contains(neighbour)) {
                dfs(neighbour, visited, component);
            }
        }
    }

    /**
     * Checks if every component has a police station and a hospital
     *
     * @return true if every component has a street connected
     * to a police station and a street connected to a hospital, false otherwise
     */
    public boolean hasValidSites() {
        List<List<BasicVertex>> components = getComponents();
        for (int i = 0; i < components.size(); i++) {
            boolean hasHospital = false;
            boolean hasPoliceStation = false;
            for (BasicVertex v : components.get(i)) {
                for (BasicVertex neighbour : v.getNeighbours()) {
                    if (neighbour instanceof Hospital) {
                        hasHospital = true;
                    }
                    if (neighbour instanceof PoliceStation) {
                        hasPoliceStation = true;
                    }
                }
            }
            if (!hasHospital || !hasPoliceStation) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper Method for hasValidSites()
     *
     * @return all the components
     */
    List<List<BasicVertex>> getComponents() {
        Set<BasicVertex> visited = new HashSet<>();
        List<List<BasicVertex>> components = new ArrayList<>();

        for (BasicVertex[] row : siteMap.getSparseVertexArray()) {
            for (BasicVertex v : row) {
                //TODO: das ungleich null unbedingt noch in > 0 ändern
                if (v instanceof BasicStreet && ((BasicStreet) v).getSpeedLimit() != 0 && !visited.contains(v)) {
                    List<BasicVertex> newComponent = new ArrayList<>();
                    dfs(v, visited, newComponent);
                    components.add(newComponent);
                }
            }
        }
        return components;
    }

    public SparseMap getSiteMap() {
        return siteMap;
    }

    public void setSiteMap(SparseMap siteMap) {
        this.siteMap = siteMap;
    }

    /**
     * Checks for the lexicographically smaller ArrayList
     *
     * @param a ArrayList of BasicVertices
     * @param b the other ArrayList of BasicVertices
     * @return the smaller ArrayList
     */
    public ArrayList<BasicVertex> getMinLexi(ArrayList<BasicVertex> a, ArrayList<BasicVertex> b) {
        List<Integer> valuesA = new ArrayList<>();
        for (BasicVertex v : a) {
            valuesA.add(v.getValue());
        }
        Collections.sort(valuesA);

        List<Integer> valuesB = new ArrayList<>();
        for (BasicVertex v : b) {
            valuesB.add(v.getValue());
        }
        Collections.sort(valuesB);

        for (int i = 0; i < Math.min(valuesA.size(), valuesB.size()); i++) {
            int compare = Integer.compare(valuesA.get(i), valuesB.get(i));
            if (compare < 0) {
                return a;
            } else if (compare > 0) {
                return b;
            }
        }

        if (valuesA.size() < valuesB.size()) {
            return a;
        } else if (valuesB.size() < valuesA.size()) {
            return b;
        }


        return a;
    }


    /**
     * Computes a set of street nodes that, when replaced with green spaces, increases the number
     * of connected components in the siteMap from 1 to at least 2.
     * The set must meet the following conditions:
     * <ol>
     *     <li>Contains at most k street nodes.</li>
     *     <li>The street nodes in the set are not adjacent to each other.</li>
     *     <li>Replacing the street nodes in the set with green spaces increases the number of connected components in the siteMap from 1 to at least 2.</li>
     *     <li>The set is lexicographically smallest when sorted by the value of the nodes.</li>
     *     <li>The set is sorted in ascending order by the value of the nodes.</li>
     * </ol>
     *
     * @param k the maximum number of street nodes to include in the set
     * @return an array of BasicStreet objects representing the cut set, or an empty array if no such set exists
     * @throws IllegalStateException if there are more than 15 road nodes in the siteMap
     */
    public BasicStreet[] getCutSet(int k) {
        List<BasicStreet> allStreets = getAllStreets();
        if (allStreets.size() > 15) {
            throw new IllegalStateException("There should never be more than 15 road nodes in siteMap.");
        }

        List<List<BasicStreet>> combinations = new ArrayList<>();


        List<BasicStreet> minLexiSet = null;
        int originalComponents = countComponents();
        generateCombinations(allStreets, new ArrayList<>(), combinations, k, originalComponents);

        for (List<BasicStreet> combo : combinations) {
            if (isValidCutSet(combo, originalComponents)) {
                if (minLexiSet == null || isLexicographicallySmaller(combo, minLexiSet)) {
                    minLexiSet = new ArrayList<>(combo);
                }
            }
        }

        if (minLexiSet == null) {
            return new BasicStreet[0];
        }

        minLexiSet.sort(Comparator.comparingInt(BasicVertex::getValue));

        return minLexiSet.toArray(new BasicStreet[0]);
    }

    private List<BasicStreet> getAllStreets() {
        List<BasicStreet> streets = new ArrayList<>();
        for (BasicVertex[] row : siteMap.getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    streets.add((BasicStreet) vertex);
                }
            }
        }
        return streets;
    }


    /**
     * Generates all combinations of street nodes up to a specified size and stores them in a list.
     *
     * @param allStreets         the list of all street nodes
     * @param current            the current combination of street nodes
     * @param combinations       the list to store all generated combinations
     * @param k                  the maximum size of the combinations
     * @param originalComponents the original number of components in the siteMap
     */
    private void generateCombinations(List<BasicStreet> allStreets, List<BasicStreet> current, List<List<BasicStreet>> combinations, int k, int originalComponents) {
        if (current.size() > 0 && current.size() <= k && !containsAdjacentNodes(current)) {
            combinations.add(new ArrayList<>(current));
        }
        if (current.size() == k) return;
        int lastAddedIndex = allStreets.indexOf(current.isEmpty() ? null : current.get(current.size() - 1));
        for (int i = lastAddedIndex + 1; i < allStreets.size(); i++) {
            BasicStreet street = allStreets.get(i);
            if (current.isEmpty() || (!current.contains(street) && !isAdjacent(street, current))) {
                List<BasicStreet> newCurrent = new ArrayList<>(current);
                newCurrent.add(street);
                generateCombinations(allStreets, newCurrent, combinations, k, originalComponents);
            }
        }
    }

    private boolean isAdjacent(BasicStreet street, List<BasicStreet> current) {
        for (BasicStreet s : current) {
            if (areDirectNeighbours(street, s)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAdjacentNodes(List<BasicStreet> combination) {
        for (int i = 0; i < combination.size(); i++) {
            for (int j = i + 1; j < combination.size(); j++) {
                if (areDirectNeighbours(combination.get(i), combination.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean areDirectNeighbours(BasicStreet s1, BasicStreet s2) {
        // Check if two streets are directly adjacent
        int rowDiff = Math.abs(s1.getPosition().getRow() - s2.getPosition().getRow());
        int colDiff = Math.abs(s1.getPosition().getColumn() - s2.getPosition().getColumn());
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }


    /**
     * Checks if replacing a given street node with a green space increases the number of components in the siteMap.
     *
     * @param street             the street node to be replaced
     * @param originalComponents the original number of components in the siteMap
     * @return true if replacing the street node increases the number of components, false otherwise
     */
    private boolean isStreetEffective(BasicStreet street, int originalComponents) {
        siteMap.replaceVertex(street.getPosition(), new BasicGreen(street.getPosition().getRow(), street.getPosition().getColumn(), street.getValue()));
        int newComponents = countComponents();
        siteMap.replaceVertex(street.getPosition(), street);

        return newComponents > originalComponents;
    }

    /**
     * Checks if a combination of street nodes forms a valid cut set that increases the number of components in the siteMap.
     *
     * @param combo              the combination of street nodes
     * @param originalComponents the original number of components in the siteMap
     * @return true if the combination forms a valid cut set, false otherwise
     */
    private boolean isValidCutSet(List<BasicStreet> combo, int originalComponents) {
        // Temporarily replace streets with greens
        Map<BasicStreet, BasicVertex> originalVertices = new HashMap<>();
        for (BasicStreet street : combo) {
            originalVertices.put(street, siteMap.getBasicVertex(street.getPosition()).orElse(null));
            siteMap.replaceVertex(street.getPosition(), new BasicGreen(street.getPosition().getRow(), street.getPosition().getColumn(), street.getValue()));
        }

        int newComponents = countComponents2();

        for (Map.Entry<BasicStreet, BasicVertex> entry : originalVertices.entrySet()) {
            siteMap.replaceVertex(entry.getKey().getPosition(), entry.getValue());
        }

        return newComponents > originalComponents;
    }


    /**
     * Compares two lists of street nodes lexicographically.
     *
     * @param combo1 the first list of street nodes
     * @param combo2 the second list of street nodes
     * @return true if the first list is lexicographically smaller, false otherwise
     */
    private boolean isLexicographicallySmaller(List<BasicStreet> combo1, List<BasicStreet> combo2) {
        for (int i = 0; i < Math.min(combo1.size(), combo2.size()); i++) {
            if (combo1.get(i).getValue() != combo2.get(i).getValue()) {
                return combo1.get(i).getValue() < combo2.get(i).getValue();
            }
        }
        return combo1.size() < combo2.size();
    }

    /**
     * Counts the number of connected components in the siteMap considering only street nodes.
     *
     * @return the number of connected components
     */
    int countComponents2() {
        Set<BasicVertex> visited = new HashSet<>();
        int components = 0;

        for (int i = 0; i < siteMap.getSparseVertexArray().length; i++) {
            for (int j = 0; j < siteMap.getSparseVertexArray()[i].length; j++) {
                BasicVertex vertex = siteMap.getSparseVertexArray()[i][j];
                if (vertex instanceof BasicStreet && !visited.contains(vertex)) {

                    dfs(vertex, visited);
                    components++;
                }
            }
        }
        return components;
    }


    /**
     * Performs a depth first search  to mark all reachable street nodes from a given starting vertex.
     *
     * @param vertex  the starting vertex
     * @param visited the set of already visited vertices
     */
    private void dfs(BasicVertex vertex, Set<BasicVertex> visited) {

        visited.add(vertex);

        for (BasicVertex neighbor : vertex.getNeighbours()) {
            if (neighbor instanceof BasicStreet && !visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }
}

