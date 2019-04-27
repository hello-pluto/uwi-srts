/*
 * Copyright (c) 2019. Razor Sharp Software Solutions
 *
 * Azel Daniel (816002285)
 * Michael Bristol (816003612)
 * Amanda Seenath (816002935)
 *
 * INFO 3604
 * Project
 *
 * UWI Shuttle Routing and Tracking System
 */

package edu.uwi.sta.srts.utils;

import java.io.Serializable;

public class SimpleLocation implements Serializable {

    private double latitude;
    private double longitude;

    /**
     * Default constructor
     */
    public SimpleLocation() {
        this.latitude = 0;
        this.longitude = 0;
    }

    /**
     * Constructor that requires a location's latitude and longitude
     * @param latitude The latitude coordinate of the location
     * @param longitude The longitude coordinate of the location
     */
    public SimpleLocation(double latitude, double longitude) {
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

