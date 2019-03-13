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
 * This class represents a route in the system
 */

package edu.uwi.sta.srts.models;

public class Route implements Model {

    private String routeId;

    private String name;

    private int frequency;

    /**
     * Default constructor for Firebase
     */
    public Route(){

    }

    /**
     * Constructor that fetches the route corresponding to the given routeId
     * @param routeId The routeId of the route to fetch
     */
    public Route(String routeId){
        // TODO: Fetch route from database corresponding to routeId
    }

    /**
     * Constructor that create a new route object and requires a route name and frequency
     * @param name The name of the route e.g. JFK-SAL
     * @param frequency The number of minutes between each shuttle arrival e.g. 15 (minutes)
     */
    public Route(String name, int frequency){
        this.name = name;
        this.frequency = frequency;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int hashCode() {
        return this.getRouteId().hashCode();
    }

    @Override
    public void save() {
        // TODO: Sync route data with database
    }
}
