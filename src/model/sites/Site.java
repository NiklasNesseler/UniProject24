package model.sites;

import model.BasicStreet;

public class Site extends BasicStreet {
    public Site(int row, int column, int value, int speedLimit) {
        super(row, column, value, 0);
    }
}
