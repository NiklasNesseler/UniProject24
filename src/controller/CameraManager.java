package controller;

import model.*;
import model.cameras.Camera;

import java.util.*;

/**
 * The CameraManager class is responsible for managing and optimizing the placement of cameras on a SparseMap.
 * It calculates the minimal and cost-effective camera coverage for observing streets.
 */

public class CameraManager {
    /** The SparseMap object representing the observed map. */
    private SparseMap observedMap;

    /** A 2D array representing the costs of placing a camera at specific positions on the map. */
    private int[][] cameraCosts;

    /** Enum representing the types of basic vertices on the map. */
    public enum BasicVertexType {
        BASICVERTEX,
        BASICSTREET,
        BASICBUILDING,
        BASICGREEN
    }

    /**
     * Constructs a CameraManager with the specified observed map and camera costs.
     * This constructor initializes the observedMap and cameraCosts attributes.
     *
     * @param observedMap the SparseMap object to be observed
     * @param cameraCosts a 2D array representing the costs of placing cameras
     */
    public CameraManager(SparseMap observedMap, int[][] cameraCosts) {
        this.observedMap = observedMap;
        this.cameraCosts = cameraCosts;
    }

    /**
     * Returns the observed map.
     *
     * @return the observed map
     */
    public SparseMap getObservedMap() {
        return observedMap;
    }


    /**
     * Sets the observed map.
     *
     * @param observedMap the observed map to be set
     */
    public void setObservedMap(SparseMap observedMap) {
        this.observedMap = observedMap;
    }


    /**
     * Returns the camera costs.
     *
     * @return the camera costs
     */
    public int[][] getCameraCosts() {
        return cameraCosts;
    }


    /**
     * Sets the camera costs.
     *
     * @param cameraCosts the camera costs to be set
     */
    public void setCameraCosts(int[][] cameraCosts) {
        this.cameraCosts = cameraCosts;
    }


    /**
     * Determines if the given cameras cover all the streets in the observed map.
     *
     * @param cameras a list of cameras to be placed on the map
     * @return true if all streets are covered by the cameras, false otherwise
     * @throws IllegalArgumentException if observedMap or cameras is null
     */
    boolean isCameraCover(ArrayList<Camera> cameras) {
        if (observedMap == null || cameras == null) {
            throw new IllegalArgumentException("observedMap and cameras cannot be null");
        }
        Set<BasicVertex> observedStreets = new HashSet<>();
        for (BasicVertex[] row : observedMap.getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet){
                    observedStreets.add(vertex);
                }
            }
        }

        ArrayList<BasicVertex> coveredVertices = new ArrayList<>();
        for (Camera camera : cameras) {
            coveredVertices.addAll(camera.getObservedVertices());
        }

        for (BasicVertex street : observedStreets) {
            if (!coveredVertices.contains(street)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Computes a set of nodes that covers all streets of a specified type within a given range.
     *
     * @param range the range of the cameras
     * @param type the type of nodes to be covered
     * @return a sorted list of nodes that cover all streets of the specified type
     */
    public ArrayList<BasicVertex> computeCover(int range, BasicVertexType type) {
        ArrayList<BasicVertex> candidateNodes = new ArrayList<>();
        ArrayList<BasicVertex> streetNodes = new ArrayList<>();
        BasicVertex[][] vertexArray = observedMap.getSparseVertexArray();

        for (BasicVertex[] row : vertexArray) {
            for (BasicVertex vertex : row) {
                if (matchesType(vertex, type)) {
                    candidateNodes.add(vertex);
                }
                if (vertex instanceof BasicStreet) {
                    streetNodes.add(vertex);
                }
            }
        }

        ArrayList<ArrayList<BasicVertex>> combinations = generateCombinations(candidateNodes, 5);

        ArrayList<BasicVertex> bestCover = new ArrayList<>();
        for (ArrayList<BasicVertex> combination : combinations) {
            if (coversAllStreets(combination, streetNodes, range) &&
                    (bestCover.isEmpty() || combination.size() < bestCover.size() ||
                            (combination.size() == bestCover.size() && isLexicographicallySmaller(combination, bestCover)))) {
                bestCover = new ArrayList<>(combination);
            }
        }

        bestCover.sort(Comparator.comparingInt(BasicVertex::getValue));

        return bestCover;
    }

    /**
     * Checks if a vertex matches the specified type.
     *
     * @param vertex the vertex to be checked
     * @param type the type to be matched
     * @return true if the vertex matches the type, false otherwise
     */
    private boolean matchesType(BasicVertex vertex, BasicVertexType type) {
        return switch (type) {
            case BASICVERTEX -> true;
            case BASICSTREET -> vertex instanceof BasicStreet;
            case BASICBUILDING -> vertex instanceof BasicBuilding;
            case BASICGREEN -> vertex instanceof BasicGreen;
            default -> false;
        };
    }

    /**
     * Generates all combinations of the nodes with the specified maximum size.
     *
     * @param nodes the list of nodes to generate combinations from
     * @param maxSize the maximum size of the combinations
     * @return a list of combinations of nodes
     */
    private ArrayList<ArrayList<BasicVertex>> generateCombinations(ArrayList<BasicVertex> nodes, int maxSize) {
        ArrayList<ArrayList<BasicVertex>> result = new ArrayList<>();
        for (int i = 1; i <= maxSize; i++) {
            generateCombinationsHelp(nodes, i, 0, new ArrayList<>(), result);
        }
        return result;
    }

    /**
     * Helper Function to generate Combinations
     * @param nodes list of nodes to generate combinations
     * @param k size of the combination
     * @param start starting index for the combination
     * @param current current index of the combination
     * @param result the list where the combinations are stored
     */
    private void generateCombinationsHelp(ArrayList<BasicVertex> nodes, int k, int start,
                                            ArrayList<BasicVertex> current, ArrayList<ArrayList<BasicVertex>> result) {
        if (k == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < nodes.size(); i++) {
            current.add(nodes.get(i));
            generateCombinationsHelp(nodes, k - 1, i + 1, current, result);
            current.removeLast();
        }
    }


    /**
     * Helper Function for ComputeCover and ComputeMinCover that checks if all Streets are Covered by Cameras
     * @param cover the combination of vertices
     * @param streetNodes the list of street vertices
     * @param range the camera range
     * @return true if all streets are covered by cameras, false if not
     */
    private boolean coversAllStreets(ArrayList<BasicVertex> cover, ArrayList<BasicVertex> streetNodes, int range) {
        Set<BasicVertex> coveredStreets = new HashSet<>();
        for (BasicVertex cameraNode : cover) {
            for (BasicVertex streetNode : streetNodes) {
                if (cameraNode.getBasicManhattanDistance(streetNode) <= range && isVisible(cameraNode, streetNode)) {
                    coveredStreets.add(streetNode);
                }
            }
        }
        return coveredStreets.size() == streetNodes.size();
    }


    /**
     * Helper Function for CoversAllStreets that checks for Visibility as Buildings obstruct sight
     * @param start starting vertex
     * @param end ending vertex
     * @return true if the line of sight is open, false otherwise
     */
    private boolean isVisible(BasicVertex start, BasicVertex end) {
        BasicVertex[][] vertexArray = observedMap.getSparseVertexArray();
        int startRow = start.getPosition().getRow();
        int startCol = start.getPosition().getColumn();
        int endRow = end.getPosition().getRow();
        int endCol = end.getPosition().getColumn();

        if (startRow == endRow) {
            int minCol = Math.min(startCol, endCol);
            int maxCol = Math.max(startCol, endCol);
            for (int col = minCol + 1; col < maxCol; col++) {
                if (vertexArray[startRow][col] instanceof BasicBuilding) {
                    return false;
                }
            }
        } else if (startCol == endCol) {
            int minRow = Math.min(startRow, endRow);
            int maxRow = Math.max(startRow, endRow);
            for (int row = minRow + 1; row < maxRow; row++) {
                if (vertexArray[row][startCol] instanceof BasicBuilding) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }


    /**
     * Helper function for ComputeCover and ComputeMinCover that compares 2 Lists of BasicVertices Lexicographically
     * @param a 1 of the compared Lists
     * @param b The other of the compared Lists
     * @return true if a is lexicographically smaller, false otherwise
     */
    private boolean isLexicographicallySmaller(ArrayList<BasicVertex> a, ArrayList<BasicVertex> b) {
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
            int compare = Integer.compare(a.get(i).getValue(), b.get(i).getValue());
            if (compare != 0) {
                return compare < 0;
            }
        }
        return a.size() < b.size();
    }


    /**
     * Computes a minimal cost set of nodes that covers all streets within a given range.
     *
     * @param range the range of the cameras
     * @return a sorted list of nodes that cover all streets at minimal cost
     */
    public ArrayList<BasicVertex> computeMinCover(int range) {
        ArrayList<BasicVertex> streetNodes = new ArrayList<>();
        BasicVertex[][] vertexArray = observedMap.getSparseVertexArray();

        for (BasicVertex[] basicVertices : vertexArray) {
            for (BasicVertex basicVertex : basicVertices) {
                if (basicVertex instanceof BasicStreet) {
                    streetNodes.add(basicVertex);
                }
            }
        }

        ArrayList<ArrayList<BasicVertex>> combinations = generateCombinations(streetNodes, 5);

        ArrayList<BasicVertex> bestCover = new ArrayList<>();
        int minCost = Integer.MAX_VALUE;

        for (ArrayList<BasicVertex> combination : combinations) {
            if (coversAllStreets(combination, streetNodes, range)) {
                int currentCost = calculateCost(combination);
                if (currentCost < minCost ||
                        (currentCost == minCost && combination.size() < bestCover.size()) ||
                        (currentCost == minCost && combination.size() == bestCover.size() && isLexicographicallySmaller(combination, bestCover))) {
                    bestCover = new ArrayList<>(combination);
                    minCost = currentCost;
                }
            }
        }

        bestCover.sort(Comparator.comparingInt(BasicVertex::getValue));

        return bestCover;
    }


    /**
     * Helper Function for ComputeMinCover that calculates COsts of placing a camera on a BasicVertex
     * @param cover list of cover vertices
     * @return the total cost of placing camera on the cover vertices
     */
    private int calculateCost(ArrayList<BasicVertex> cover) {
        int totalCost = 0;
        for (BasicVertex vertex : cover) {
            totalCost += cameraCosts[vertex.getPosition().getRow()][vertex.getPosition().getColumn()];
        }
        return totalCost;
    }
}
