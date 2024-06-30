package controller;

import model.*;
import model.cameras.Camera;

import java.util.*;


public class CameraManager {
    private SparseMap observedMap;
    private int[][] cameraCosts;
    public enum BasicVertexType {
        BASICVERTEX,
        BASICSTREET,
        BASICBUILDING,
        BASICGREEN
    }

    public CameraManager(SparseMap observedMap, int[][] cameraCosts) {
        this.observedMap = observedMap;
        this.cameraCosts = cameraCosts;
    }

    public SparseMap getObservedMap() {
        return observedMap;
    }

    public void setObservedMap(SparseMap observedMap) {
        this.observedMap = observedMap;
    }

    public int[][] getCameraCosts() {
        return cameraCosts;
    }

    public void setCameraCosts(int[][] cameraCosts) {
        this.cameraCosts = cameraCosts;
    }

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

    public ArrayList<BasicVertex> computeCover(int range, BasicVertexType type) {
        ArrayList<BasicVertex> candidateNodes = new ArrayList<>();
        ArrayList<BasicVertex> streetNodes = new ArrayList<>();
        BasicVertex[][] vertexArray = observedMap.getSparseVertexArray();

        // 1. Collect candidate nodes and street nodes
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

    private boolean matchesType(BasicVertex vertex, BasicVertexType type) {
        return switch (type) {
            case BASICVERTEX -> true;
            case BASICSTREET -> vertex instanceof BasicStreet;
            case BASICBUILDING -> vertex instanceof BasicBuilding;
            case BASICGREEN -> vertex instanceof BasicGreen;
            default -> false;
        };
    }

    private ArrayList<ArrayList<BasicVertex>> generateCombinations(ArrayList<BasicVertex> nodes, int maxSize) {
        ArrayList<ArrayList<BasicVertex>> result = new ArrayList<>();
        for (int i = 1; i <= maxSize; i++) {
            generateCombinationsHelp(nodes, i, 0, new ArrayList<>(), result);
        }
        return result;
    }

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

    private boolean isLexicographicallySmaller(ArrayList<BasicVertex> a, ArrayList<BasicVertex> b) {
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
            int compare = Integer.compare(a.get(i).getValue(), b.get(i).getValue());
            if (compare != 0) {
                return compare < 0;
            }
        }
        return a.size() < b.size();
    }

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

    private int calculateCost(ArrayList<BasicVertex> cover) {
        int totalCost = 0;
        for (BasicVertex vertex : cover) {
            totalCost += cameraCosts[vertex.getPosition().getRow()][vertex.getPosition().getColumn()];
        }
        return totalCost;
    }
}
