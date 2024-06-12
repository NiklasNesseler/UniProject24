package controller;

import model.BasicVertex;
import model.Position2D;
import model.SparseMap;

import java.util.ArrayList;
import java.util.Optional;

public class SiteManager {
    SparseMap sitemap;
    public SiteManager(SparseMap sitemap) {
        this.sitemap = sitemap;
    }
//    void putSites(ArrayList<Position2D> positionList) {
//        for (Position2D position : positionList) {
//            Optional<BasicVertex> vertex = sitemap.getBasicVertex(position);
//            if (vertex.isPresent()) {
//            }
//        }
//    }
}
