package model.cars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class managing the progression of time steps acting as the publisher in an observer pattern
 */
public class TimeStepper {
    /**
     * current time step, initialized as 0
     */
    private int timeStep;
    /**
     * the maximum time step that will get reached
     */
    private int end;
    /**
     * List of cars sorted by their ID
     */
    private ArrayList<Car> subscribers;


    /**
     * Constructor of the time stepper with the specified end time
     * @param end maximum time step
     */
    public TimeStepper(int end) {
        this.timeStep = 0;
        this.end = end;
        this.subscribers = new ArrayList<>();
    }

    /**
     * Advances the time step by one
     */
    public void oneStepForward() {
        if (timeStep < end) {
            timeStep++;
            updateSubscribers();
        }
    }

    /**
     * Notifies the cars to update to the current time step
     */
    void updateSubscribers() {
        for (Car car : subscribers) {
            car.update(timeStep);
        }
    }

    /**
     * Adds a car to the list of subscribers
     * @param car the car to be added
     */
    public void addSubscriber(Car car) {
        subscribers.add(car);
        subscribers.sort(Comparator.comparingInt(Car::getId));
    }

    /**
     * Removes a car from the list of subscribers
     * @param car the car to be removed
     */
    public void removeSubscriber(Car car) {
        subscribers.remove(car);
    }


    /**
     * @return the current time step
     */
    public int getTimeStep() {
        return timeStep;
    }

    /**
     * Set the current time step
     * @param timeStep current time step
     */
    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }


    /**
     * @return the end time step
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets the end time step
     * @param end end time step
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @return the list of cars
     */
    public ArrayList<Car> getSubscribers() {
        return subscribers;
    }

    /**
     * Sets the list of cars
     * @param subscribers cars
     */
    public void setSubscribers(ArrayList<Car> subscribers) {
        this.subscribers = subscribers;
    }
}
