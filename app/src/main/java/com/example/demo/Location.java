package com.example.demo;

import java.io.Serializable;

public class Location implements Serializable {

    private long tripId;
    private long locationId;
    private String name;
    private String time;

    public Location(long tripId,long locationId,String name, String time) {

        this.tripId=tripId;
        this.locationId=locationId;
        this.name = name;
        this.time = time;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
