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
 * This class represents a location on the map
 */
package edu.uwi.sta.srts.models;

public class Location {
    private String locationName;
    private double lat;
    private double longT;


    public String getLocationName(){
        return locationName;
    }

    public double getLat() {
        return lat;
    }

    public double getLongT() {
        return longT;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLongT(double longT) {
        this.longT = longT;
    }


}

