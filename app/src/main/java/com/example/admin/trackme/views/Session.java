package com.example.admin.trackme.views;

import java.util.Date;

public class Session {
    private int id;
    private double distance;
    private float averageSpeed;
    private long startTime;
    private long endTime;
    private byte[] image;

    // Empty constructor
    public Session() {

    }

    // constructor
    public Session(int id, double distance,float averageSpeed,long startTime,long endTime, byte[] image) {
        this.id = id;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.image = image;

    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
