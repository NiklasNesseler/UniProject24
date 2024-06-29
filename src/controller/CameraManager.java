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

    ArrayList<BasicVertex> computeCover(int range, BasicVertexType type) {
        ArrayList<BasicVertex> relevantVertices = new ArrayList<>();

        for (BasicVertex[] row : observedMap.getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                if (matchesType(vertex, type)) {
                    relevantVertices.add(vertex);
                }
            }
        }
        return findOptimalCover(relevantVertices, range);
    }

    private ArrayList<BasicVertex> findOptimalCover(ArrayList<BasicVertex> relevantVertices, int range) {
        ArrayList<BasicVertex> bestCover = new ArrayList<>();
        for (int size = 1; size <= 5; size++) {
            List<List<BasicVertex>> combinations = generateCombinations(relevantVertices, size);
            for (List<BasicVertex> combination : combinations) {
                if (coversAllStreets(combination, range)) {
                    if (bestCover.isEmpty() || isBetterCombination(combination, bestCover)) {
                        bestCover = new ArrayList<>(combination);
                    }
                }
            }

            if (!bestCover.isEmpty()) {
                break;
            }
        }
        bestCover.sort(Comparator.comparingInt(BasicVertex::getValue));
        return bestCover;

    }

    private boolean isBetterCombination(List<BasicVertex> newCombination, ArrayList<BasicVertex> bestCover) {
        List<Integer> newValues = newCombination.stream().map(BasicVertex::getValue).sorted().toList();
        List<Integer> bestValues = bestCover.stream().map(BasicVertex::getValue).sorted().toList();

        for (int i = 0; i < newValues.size(); i++) {
            int compare = newValues.get(i).compareTo(bestValues.get(i));
            if (compare < 0) {
                return true;
            } else if (compare > 0) {
                return false;
            }
        }
        return false;
    }

    private boolean coversAllStreets(List<BasicVertex> combination, int range) {
        ArrayList<Camera> cameras = new ArrayList<>();
        for (BasicVertex vertex : combination) {
            cameras.add(new Camera(range, vertex.getPosition(), observedMap));
        }

        return isCameraCover(cameras);
    }

    private List<List<BasicVertex>> generateCombinations(ArrayList<BasicVertex> relevantVertices, int size) {
        List<List<BasicVertex>> combinations = new ArrayList<>();
        generateCombinationsHelp(relevantVertices, new ArrayList<>(), combinations, size, 0);
        return combinations;
    }

    private void generateCombinationsHelp(ArrayList<BasicVertex> relevantVertices, List<BasicVertex> current, List<List<BasicVertex>> result, int size, int start) {
        if (current.size() == size) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < relevantVertices.size(); i++) {
            current.add(relevantVertices.get(i));
            generateCombinationsHelp(relevantVertices, current, result, size, i + 1);
            current.removeLast();
        }
    }

    private boolean matchesType(BasicVertex vertex, BasicVertexType type) {
        return switch (type) {
            case BASICVERTEX -> vertex != null;
            case BASICSTREET -> vertex instanceof BasicStreet;
            case BASICBUILDING -> vertex instanceof BasicBuilding;
            case BASICGREEN -> vertex instanceof BasicGreen;
            default -> false;
        };
    }

    ArrayList<BasicVertex> computeMinCover(int range) {
        ArrayList<BasicVertex> allStreets = new ArrayList<>();

        for (BasicVertex[] row : observedMap.getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    allStreets.add(vertex);
                }
            }
        }

        return findOptimalCostCover(allStreets, range);
    }

    private ArrayList<BasicVertex> findOptimalCostCover(ArrayList<BasicVertex> allStreets, int range) {
        ArrayList<BasicVertex> bestCover = new ArrayList<>();
        int minCost = Integer.MAX_VALUE;

        for (int size = 1; size <= 5; size++) {
            List<List<BasicVertex>> combinations = generateCombinations(allStreets, size);
            for (List<BasicVertex> combination : combinations) {
                if (coversAllStreets(combination, range)) {
                    int cost = calculateTotalCost(combination);
                    if (cost < minCost || (cost == minCost && isBetterCombination(combination, bestCover))) {
                        minCost = cost;
                        bestCover = new ArrayList<>(combination);

                    }
                }
            }
            if (!bestCover.isEmpty()) {
                break;
            }
        }
        bestCover.sort(Comparator.comparingInt(BasicVertex::getValue));
        return bestCover;
    }

    private int calculateTotalCost(List<BasicVertex> combination) {
        int totalCost = 0;
        for (BasicVertex vertex : combination) {
            Position2D position = vertex.getPosition();
            totalCost += cameraCosts[position.getRow()][position.getColumn()];
        }
        return totalCost;
    }


}
