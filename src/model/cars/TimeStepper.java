package model.cars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TimeStepper {
    private int timeStep;
    private int end;
    private ArrayList<Car> subscribers;

    public TimeStepper(int end) {
        this.timeStep = 0;
        this.end = end;
        this.subscribers = new ArrayList<>();
    }

    public void oneStepForward() {
        if (timeStep < end) {
            timeStep++;
            updateSubscribers();
        }
    }

    void updateSubscribers() {
        for (Car car : subscribers) {
            car.update(timeStep);
        }
    }

    public void addSubscriber(Car car) {
        subscribers.add(car);
        subscribers.sort(Comparator.comparingInt(Car::getId));
    }

    public void removeSubscriber(Car car) {
        subscribers.remove(car);
    }



    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public ArrayList<Car> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(ArrayList<Car> subscribers) {
        this.subscribers = subscribers;
    }
}
