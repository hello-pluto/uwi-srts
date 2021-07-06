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
 * This class acts as a bridge between a list of route models and their view.
 */

package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.views.View;

public class RoutesController extends Controller{

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

    public void addRoute(Route route){
        ((Routes)this.model).addRoute(route);
    }

    public void removeRoute(int index){
        ((Routes)this.model).removeRoute(index);
    }

    public Route getRouteByName(String routeName){
        for(Route route : getRoutes()){
            if(route.getName().equals(routeName)){
                return route;
            }
        }
        return null;
    }

    public RouteController getRouteController(int index, View view){
        return new RouteController(getRoutes().get(index), view);
    }

    public Route filter(String routeId){
        for (Route route: ((Routes)this.model).getRoutes()) {
            if(route.getId().equals(routeId)){
                return route;
            }
        }

        return null;
    }
}