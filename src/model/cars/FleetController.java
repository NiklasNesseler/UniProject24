package model.cars;

import model.Position2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FleetController {
    private ArrayList<Car> cars;
    private TimeStepper timeStepper;

    public FleetController(ArrayList<Car> cars, int endTime) {
        this.cars = cars;
        this.timeStepper = new TimeStepper(endTime);


        for (Car car : cars) {
            timeStepper.addSubscriber(car);
            car.update(0);
        }
    }

    public void runUntilTimeStep(int targetTimeStep) {
        while (timeStepper.getTimeStep() < targetTimeStep) {
            timeStepper.oneStepForward();
            solveConflicts();
        }
    }

    private void solveConflicts() {
        Map<Position2D, ArrayList<Car>> position2DArrayListMap = new HashMap<>();
        for (Car car : cars) {
            Position2D current = car.getCurrentPos();
            if (!position2DArrayListMap.containsKey(current)) {
                position2DArrayListMap.put(current, new ArrayList<>());
            }
            position2DArrayListMap.get(current).add(car);
        }

        for (Map.Entry<Position2D, ArrayList<Car>> entry : position2DArrayListMap.entrySet()) {
            ArrayList<Car> carList = entry.getValue();
            if (carList.size() > 1) {
                Car carWithLowestID = carList.getFirst();
                for (Car car : carList) {
                    if (car.getId() < carWithLowestID.getId()) {
                        carWithLowestID = car;
                    }
                }

                for (Car car : carList) {
                    if (car != carWithLowestID) {
                        car.onePositionBack();
                    }
                }
            }
        }
    }


    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public TimeStepper getTimeStepper() {
        return timeStepper;
    }

    public void setTimeStepper(TimeStepper timeStepper) {
        this.timeStepper = timeStepper;
    }
}
