package controller.advanced;

import controller.BasicController;
import model.advanced.AdvancedMap;

/**
 * The AdvancedController class is responsible for initializing and managing an AdvancedMap.
 * It initializes the map using data provided through its constructor.
 * @author Niklas Nesseler
 */

public class AdvancedController {
    /** The AdvancedMap object managed by this controller. */
    AdvancedMap advancedMap;

    /**
     * Constructs an AdvancedController with the specified base data, value data, and street codes.
     * This constructor calls the initAdvancedMap method to initialize the advancedMap attribute.
     *
     * @param baseData a 2D array representing the base data for the map
     * @param valueData a 2D array representing the value data for the map
     * @param streetCodes a 2D array representing the street codes for the map
     */

    public AdvancedController(int[][] baseData, int[][] valueData, int[][] streetCodes) {
        initAdvancedMap(baseData, valueData, streetCodes);
    }

    /**
     * Initializes the AdvancedMap attribute by performing the following steps:
     * <ol>
     *     <li>Creates a BasicController object using the baseData and valueData parameters.</li>
     *     <li>Initializes the advancedMap attribute with an AdvancedMap object, using the completeBasicMap from the BasicController.</li>
     *     <li>Calls the buildAdvancedStreets method on the advancedMap object with the streetCodes parameter.</li>
     * </ol>
     *
     * @param baseData a 2D array representing the base data for the map
     * @param valueData a 2D array representing the value data for the map
     * @param streetCodes a 2D array representing the street codes for the map
     */

    void initAdvancedMap(int[][] baseData, int[][] valueData, int[][] streetCodes) {
        BasicController basicController = new BasicController(baseData, valueData);
        this.advancedMap = new AdvancedMap(basicController.getCompleteBasicMap());
        this.advancedMap.buildAdvancedStreets(streetCodes);
    }

    /**
     * Returns the AdvancedMap object managed by this controller.
     *
     * @return the advancedMap object
     */
    public AdvancedMap getAdvancedMap() {
        return advancedMap;
    }


    /**
     * Sets the AdvancedMap object managed by this controller.
     *
     * @param advancedMap the AdvancedMap object to be set
     */
    public void setAdvancedMap(AdvancedMap advancedMap) {
        this.advancedMap = advancedMap;
    }
}
