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
 * This class acts as a bridge between a single route model and its view.
 */

package edu.uwi.sta.srts.controllers;

import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.views.View;

public class RouteController extends Controller{

    /**
     * Constructor that requires the route model and its corresponding view
     * @param model The route model
     * @param view The corresponding view
     */
    public RouteController(Route model, View view) {
        super(model, view);
    }

    public String getRouteId() {
        return ((Route)this.model).getId();
    }

    public void setRouteId(String routeId) {
        ((Route)this.model).setId(routeId);
    }

    public String getRouteName() {
        return ((Route)((Route)this.model)).getName();
    }

    public void setRouteName(String name) {
        ((Route)this.model).setName(name);
    }

    public int getRouteFrequency() {
        return ((Route)this.model).getFrequency();
    }

    public void setRouteFrequency(int frequency) {
        ((Route)this.model).setFrequency(frequency);
    }
}
