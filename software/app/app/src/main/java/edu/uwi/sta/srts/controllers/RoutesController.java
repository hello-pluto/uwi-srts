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

package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.View;

public class RoutesController extends Controller {

    /**
     * Constructor that requires the routes model and its corresponding view
     * @param model The list of route models
     * @param view The corresponding view
     */
    public RoutesController(Routes model, View view) {
        super(model, view);
    }

    public ArrayList<Route> getRoutes() {
        return ((Routes)this.model).getRoutes();
    }

    /**
     * Method that returns the route controller for a given route
     * @param position The index of the route
     * @param view The view to link the controller to
     * @return A newly created RouteController object
     */
    public RouteController getRouteController(int position, View view){
        return new RouteController(getRoutes().get(position), view);
    }

    /**
     * Method that returns the route associated with the given route id
     * @param routeId The route id of the route
     * @return The route that has the corresponding route id if it exists or null otherwise.
     */
    public Route getRoute(String routeId){
        for (Route route: ((Routes)this.model).getRoutes()) {
            if(route.getId().equals(routeId)){
                return route;
            }
        }

        return null;
    }
}