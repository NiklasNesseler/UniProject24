package model.cars;

import model.Position2D;

import java.util.ArrayList;

/**
 * Class representing a vehicle on the map
 */
public class Car {
    /**
     * Unique identifier for the car
     */
    private int id;
    /**
     * Array List representing the cars trip
     */
    private ArrayList<Position2D> trip;
    /**
     * Represents the time step at which the car spawns
     */
    final int spawnTime;
    /**
     * Represents the spawn position of the car
     */
    final Position2D spawnPos;
    /**
     * Represents the current time step of the car
     */
    private int currentTime;
    /**
     * Represents the current position of the car
     */
    private Position2D currentPos;
    private Position2D previousPos;


    /**
     * Constructor for the car with the specified parameters
     *
     * @param id        unique identifier for the car
     * @param trip      the list of positions representing the cars trip
     * @param spawnTime the time step at which the car spawns
     */
    public Car(int id, ArrayList<Position2D> trip, int spawnTime) {
        this.id = id;
        this.trip = new ArrayList<>(trip);
        this.spawnTime = spawnTime;
        this.currentTime = 0;
        this.currentPos = new Position2D(-1, -1);
        this.spawnPos = trip.getFirst();
        this.previousPos = new Position2D(-1, -1);
    }

    /**
     * Updates the cars state in the map to the specific time step
     *
     * @param nextTimeStep the next time step to update to
     */
    public void update(int nextTimeStep) {
        this.previousPos = this.currentPos;  // Store current position as previous
        this.currentTime = nextTimeStep;

        if (currentTime >= spawnTime) {
            int tripIndex = currentTime - spawnTime;
            if (tripIndex < trip.size()) {
                currentPos = trip.get(tripIndex);
            } else {
                currentPos = trip.getLast();
            }
        }
    }

    /**
     * Moves the car one step back on its trip
     * If the car is in its initial position or not yet spawned, it does not move
     */
    public void onePositionBack() {
        if (currentTime > spawnTime) {
            this.currentPos = this.previousPos;
            int previousIndex = currentTime - spawnTime - 2;
            if (previousIndex >= 0) {
                this.previousPos = trip.get(previousIndex);
            } else {
                this.previousPos = new Position2D(-1, -1);
            }
        }
    }

    /**
     * @return the cars ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the cars ID
     *
     * @param id gets set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the ArrayList of the cars trip
     */
    public ArrayList<Position2D> getTrip() {
        return trip;
    }

    /**
     * Sets the cars trip
     *
     * @param trip
     */
    public void setTrip(ArrayList<Position2D> trip) {
        this.trip = trip;
    }

    /**
     * @return the cars spawn time
     */
    public int getSpawnTime() {
        return spawnTime;
    }

    /**
     * @return the cars spawn position
     */
    public Position2D getSpawnPos() {
        return spawnPos;
    }

    /**
     * @return the current time step
     */
    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * Sets the current time step
     *
     * @param currentTime step
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * @return the current position of a car
     */
    public Position2D getCurrentPos() {
        return currentPos;
    }

    /**
     * Set the current position of a car
     *
     * @param currentPos of a car
     */
    public void setCurrentPos(Position2D currentPos) {
        this.currentPos = currentPos;
    }

    public Position2D getPreviousPos() {
        return previousPos;
    }

    public void setPreviousPos(Position2D previousPos) {
        this.previousPos = previousPos;
    }
}
