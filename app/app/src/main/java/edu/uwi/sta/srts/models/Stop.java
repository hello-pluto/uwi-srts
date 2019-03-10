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
 * This class represents a stop in a route in the system
 */

package edu.uwi.sta.srts.models;

public class Stop {

    private String stopId;

    private String routeId;

    private Location location;

    /**
     * Default constructor for Firebase
     */
    public Stop(){

    }

    /**
     * Constructor that requires the stop's route id and a location
     * @param routeId The route that this stop belongs to
     * @param location The location of the stop
     */
    public Stop(String routeId, Location location){
        this.routeId = routeId;
        this.location = location;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        return this.getStopId().equals(((Stop)o).getStopId());
    }

    @Override
    public int hashCode() {
        return this.getStopId().hashCode();
    }
}
