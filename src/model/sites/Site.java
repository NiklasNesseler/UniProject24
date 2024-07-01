package model.sites;

import model.BasicStreet;


/**
 * Represents a construction site in a sparse map
 */


public class Site extends BasicStreet {
    /**
     * Constructor for the site with the specified parameters
     * @param row specified row for the site
     * @param column specified column for the site
     * @param value specified value for the site
     * @param speedLimit specified speed limit for the site being 0
     */
    public Site(int row, int column, int value, int speedLimit) {
        super(row, column, value, 0);
    }
}
