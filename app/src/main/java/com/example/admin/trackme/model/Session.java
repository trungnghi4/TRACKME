package com.example.admin.trackme.model;

import java.util.Date;

public class Session {
    private int id;
    private String distance;
    private String averageSpeed;
//    private Date startTime;
//    private Date endTime;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private byte[] image;

    // Empty constructor
    public Session() {

    }

    // constructor
    public Session(String distance,String averageSpeed,String time, byte[] image) {
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.time = time;
        this.image = image;

    }
    public Session(String distance,String averageSpeed,String time) {
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.time = time;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

}
