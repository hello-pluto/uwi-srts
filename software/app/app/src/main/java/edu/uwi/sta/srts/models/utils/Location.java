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
 * This encapsulates the information for a single location object
 */

package edu.uwi.sta.srts.models.utils;

import java.io.Serializable;

public class Location implements Serializable {



    private double latitude;

    private double longitude;

    /**
     * Default constructor for firebase
     */
    public Location() {

    }

    /**
     * Constructor that requires a location's latitude and longitude
     * @param latitude The latitude coordinate of the location
     * @param longitude The longitude coordinate of the location
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

