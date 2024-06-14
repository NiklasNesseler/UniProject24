package controller;

import model.BasicStreet;
import model.BasicVertex;
import model.Position2D;
import model.SparseMap;
import model.sites.Site;

import java.util.*;

public class SiteManager {
    SparseMap sitemap;
    public SiteManager(SparseMap sitemap) {
        this.sitemap = sitemap;
    }

    void putSites(ArrayList<Position2D> positionList) {
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = sitemap.getBasicVertex(position);
            if (vertex.isPresent()) {
                BasicVertex v = vertex.get();
                Site site = new Site(position.getRow(), position.getColumn(), v.getValue(), 0);
                sitemap.replaceVertex(position, site);
            }
        }
    }

    boolean isDisconnectedSet(ArrayList<Position2D> positionList) {
        Map<Position2D, BasicVertex> originalVertices = new HashMap<>();
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = sitemap.getBasicVertex(position);
            if (vertex.isPresent()) {
                originalVertices.put(position, vertex.get());
                sitemap.replaceVertex(position, new Site(position.getRow(), position.getColumn(), vertex.get().getValue(), 0));
            }
        }

        int initialComponents = countComponents();
        for (Position2D position : positionList) {
            sitemap.replaceVertex(position, originalVertices.get(position));
        }
        int newComponents = countComponents();

        return newComponents > initialComponents;
    }

    private int countComponents() {
        Set<BasicVertex> visited = new HashSet<>();
        int components = 0;

        for (BasicVertex[] row : sitemap.getVertexArray()) {
            for (BasicVertex v : row) {
                if (v instanceof BasicStreet && ((BasicStreet) v).getSpeedLimit() > 0 && !visited.contains(v)) {
                    components++;
                    bfs(v, visited);
                }
            }
        }
        return components;
    }

    private void bfs(BasicVertex v, Set<BasicVertex> visited) {
        Queue<BasicVertex> q = new LinkedList<>();
        q.add(v);
        visited.add(v);

        while (!q.isEmpty()) {
            BasicVertex current = q.poll();
            for (BasicVertex neighbour : current.getNeighbours()) {
                if (neighbour instanceof BasicStreet && ((BasicStreet) neighbour).getSpeedLimit() > 0 && !visited.contains(neighbour)) {
                    visited.add(neighbour);
                    q.add(neighbour);
                }
            }
        }
    }
}
