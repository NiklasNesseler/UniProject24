package model.cars;

import model.Position2D;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Class representing a manager of a fleet of cars and their progression on a map
 */
public class FleetController {
    /**
     * List of cars managed by the fleet controller
     */
    private ArrayList<Car> cars;

    /**
     * Time stepper that manages the progression of the time steps
     */
    private TimeStepper timeStepper;


    /**
     * Constructor of the FleetController with the specified list of cars and the end time
     * @param cars Array List of the cars in the fleet
     * @param endTime ending time for the time stepper
     */
    public FleetController(ArrayList<Car> cars, int endTime) {
        this.cars = cars;
        this.timeStepper = new TimeStepper(endTime);


        for (Car car : cars) {
            timeStepper.addSubscriber(car);
            car.update(0);
        }
    }


    /**
     * Runs a simulation until the specified time step
     * @param targetTimeStep the target time step
     */
    public void runUntilTimeStep(int targetTimeStep) {
        while (timeStepper.getTimeStep() < targetTimeStep) {
            timeStepper.oneStepForward();
            solveConflicts();
        }
    }


    /**
     * Solves conflicts by ensuring no two cars are on the same position
     */
    private void solveConflicts() {
        Map<Position2D, ArrayList<Car>> positionMap = new HashMap<>();

        for (Car car : cars) {
            Position2D pos = car.getCurrentPos();
            if (!pos.equals(new Position2D(-1, -1))) {
                positionMap.putIfAbsent(pos, new ArrayList<>());
                positionMap.get(pos).add(car);
            }
        }

        for (Map.Entry<Position2D, ArrayList<Car>> entry : positionMap.entrySet()) {
            ArrayList<Car> carsAtPosition = entry.getValue();
            if (carsAtPosition.size() > 1) {
                solveConflictAtPosition(carsAtPosition);
            }
        }
    }

    private void solveConflictAtPosition(ArrayList<Car> carsAtPosition) {
        Car carToStay = carsAtPosition.getFirst();
        Position2D position = carToStay.getCurrentPos();

        // Check if any car was already at this position in the previous time step
        for (Car car : carsAtPosition) {
            if (car.getPreviousPos().equals(position)) {
                carToStay = car;
                break;
            }
        }

        // If no car was at this position previously, choose the one with the lowest ID
        if (!carToStay.getPreviousPos().equals(position)) {
            for (Car car : carsAtPosition) {
                if (car.getId() < carToStay.getId()) {
                    carToStay = car;
                }
            }
        }

        // Move back all cars except the one that stays
        for (Car car : carsAtPosition) {
            if (car != carToStay) {
                car.onePositionBack();
            }
        }
    }




    /**
     * @return the list of cars in the fleet
     */
    public ArrayList<Car> getCars() {
        return cars;
    }

    /**
     * Sets the list of cars in the fleet
     * @param cars is set
     */
    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    /**
     * @return the Time stepper used
     */
    public TimeStepper getTimeStepper() {
        return timeStepper;
    }

    /**
     * Set the time stepper used
     * @param timeStepper new time stepper
     */
    public void setTimeStepper(TimeStepper timeStepper) {
        this.timeStepper = timeStepper;
    }
}
