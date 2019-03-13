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
 * This class represents a list of routes in the system
 */

package edu.uwi.sta.srts.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Routes implements Model {

    private HashMap<String, Route> routes =  new HashMap<>();

    /**
     * Default constructor that fetches all routes from the database
     */
    public Routes() {
        // TODO: Fetch data for all routes from the database.
    }

    /**
     * Constructor that accepts a local collection of routes
     * @param routes The collection of routes
     */
    public Routes(HashMap<String, Route> routes){
        this.routes.putAll(routes);
    }

    public ArrayList<Route> getRoutes() {
        return new ArrayList<Route>(this.routes.values());
    }

    public void addRoute(Route route){
        this.routes.put(route.getRouteId(), route);
    }

    public void removeRoute(Route route){
        this.routes.remove(route.getRouteId());
    }

    @Override
    public int hashCode() {
        return this.routes.hashCode();
    }

    @Override
    public void save() {
        // TODO: sync with database
    }
}
