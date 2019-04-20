/*
 * Copyright (c) 2019. Razor Sharp Software Solutions
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Michael Bristol (816003612)
 *
 * INFO 3604
 * Project
 *
 * UWI Shuttle Routing and Tracking System
 */

package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.views.View;

public class RouteStopsController extends Controller{

    /**
     * Constructor that requires the routes model and its corresponding view
     * @param model The list of route models
     * @param view The corresponding view
     */
    public RouteStopsController(RouteStops model, View view) {
        super(model, view);
    }

    public ArrayList<RouteStop> getRouteStops() {
        return ((RouteStops)this.model).getRouteStops();
    }
}
