package model.cars;

import model.Position2D;

import java.util.ArrayList;

public class Car {
    private int id;
    private ArrayList<Position2D> trip;
    final int spawnTime;
    final Position2D spawnPos;
    private int currentTime;
    private Position2D currentPos;
    private int tripIndex;


    public Car(int id, ArrayList<Position2D> trip, int spawnTime) {
        this.id = id;
        this.trip = new ArrayList<>(trip);
        this.spawnTime = spawnTime;
        this.currentTime = 0;
        this.currentPos = new Position2D(-1, -1);
        this.spawnPos = trip.getFirst();
        this.tripIndex = 0;
    }

    public void update(int nextTimeStep) {
        currentTime = nextTimeStep;

        if (currentTime < spawnTime) {
            currentPos = new Position2D(-1, -1);
            return;
        }

        if (currentTime == spawnTime) {
            currentPos = spawnPos;
            return;
        }

        if (tripIndex < trip.size() - 1) {
            tripIndex++;
            currentPos = trip.get(tripIndex);
        }
    }

    public void onePositionBack() {
        if (currentTime < spawnTime || currentPos.equals(new Position2D(-1, -1))) {
            return;
        }

        if (tripIndex > 0) {
            tripIndex--;
            currentPos = trip.get(tripIndex);
        } else {
            currentPos = spawnPos;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Position2D> getTrip() {
        return trip;
    }

    public void setTrip(ArrayList<Position2D> trip) {
        this.trip = trip;
    }

    public int getSpawnTime() {
        return spawnTime;
    }

    public Position2D getSpawnPos() {
        return spawnPos;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public Position2D getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(Position2D currentPos) {
        this.currentPos = currentPos;
    }
}
