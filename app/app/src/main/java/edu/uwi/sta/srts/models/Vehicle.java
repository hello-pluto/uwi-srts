/*
 * Copyright (c) 2019. Razor Sharp Software Solutions.
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Michael Bristol (816003612)
 *
 * INFO 3604
 * Project
 * UWI Shuttle Routing and Tracking System Project
 *
 * This class represents a driver user in the system
 */
package edu.uwi.sta.srts.models;

import android.location.Location;

public class Vehicle {
    private int seats;
    private String licencePlate;
    private Location location;
    private String driver_ID;
    private String route_ID;


    public Location getLocation() {
        return location;
    }

    public String getDriver_ID() {
        return driver_ID;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public int getSeats() {
        return seats;
    }

    public String getRoute_ID() {
        return route_ID;
    }

    public void setDriver_ID(String driver_ID) {
        this.driver_ID = driver_ID;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRoute_ID(String route_ID) {
        this.route_ID = route_ID;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
    
}
