package model.abstractClasses;

import model.BasicVertex;
import model.Square;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Interface checking if a Square or a SparseMap are Sparse
 */
public interface DensityChecker {


    /**
     * Checks a Square or SparseMap for Density Requirements
     * @return true if Requirements are met, false otherwise
     */
    public boolean isSparse();

}
