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

import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.View;

public class RouteStopController extends Controller {

    /**
     * Constructor that requires the route stop model and its corresponding view
     * @param model The route stop model
     * @param view The corresponding view
     */
    public RouteStopController(RouteStop model, View view) {
        super(model, view);
    }

    public String getRouteStopRouteId() {
        return ((RouteStop)model).getRouteId();
    }

    public void setRouteStopRouteId(String routeId) {
        ((RouteStop)model).setRouteId(routeId);
    }

    public String getRouteStopStopId() {
        return ((RouteStop)model).getStopId();
    }

    public void setRouteStopStopId(String stopId) {
        ((RouteStop)model).setStopId(stopId);
    }

    public int getRouteStopOrder() {
        return ((RouteStop)model).getOrder();
    }

    public void setRouteStopOrder(int order) {
        ((RouteStop)model).setOrder(order);
    }
}
