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


    public Car(int id, ArrayList<Position2D> trip, int spawnTime) {
        this.id = id;
        this.trip = new ArrayList<>(trip);
        this.spawnTime = spawnTime;
        this.currentTime = 0;
        this.currentPos = new Position2D(-1, -1);
        this.spawnPos = trip.getFirst();
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

        int current = trip.indexOf(currentPos);
        if (current != -1 && current < trip.size() - 1) {
            currentPos = trip.get(current + 1);
        }
    }

    public void onePositionBack() {
        if (currentTime < spawnTime || currentPos.equals(new Position2D(-1, -1))) {
            return;
        }

        int current = trip.indexOf(currentPos);
        if (current > 0) {
            currentPos = trip.get(current - 1);
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
