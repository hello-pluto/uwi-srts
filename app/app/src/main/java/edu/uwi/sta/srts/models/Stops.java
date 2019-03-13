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
 * This class represents a list of stops in the system
 */

package edu.uwi.sta.srts.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Stops implements Model {

    private HashMap<String, Stop> stops =  new HashMap<>();

    /**
     * Default constructor that fetches all stops from the database
     */
    public Stops() {
        // TODO: Fetch data for all stops from the database.
    }

    /**
     * Constructor that fetches all stops with the corresponding routeId
     * @param routeId The routeId of the stops to fetch from the database
     */
    public Stops(String routeId){
        // TODO: Fetch data from database about all stops on this route
    }

    /**
     * Constructor that accepts a local collection of stops
     * @param stops The collection of local stops
     */
    public Stops(HashMap<String, Stop> stops){
        this.stops.putAll(stops);
    }

    public ArrayList<Stop> getStops() {
        return new ArrayList<Stop>(this.stops.values());
    }

    public void addStop(Stop stop){
        this.stops.put(stop.getRouteId(), stop);
    }

    public void removeStop(Stop stop){
        this.stops.remove(stop.getStopId());
    }

    @Override
    public int hashCode() {
        return this.stops.hashCode();
    }

    @Override
    public void save() {
        // TODO: sync with database
    }
}
