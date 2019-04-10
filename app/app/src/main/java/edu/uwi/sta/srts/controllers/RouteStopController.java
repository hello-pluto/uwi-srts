package edu.uwi.sta.srts.controllers;

import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.views.View;

public class RouteStopController extends Controller {

    /**
     * Constructor that requires the route model and its corresponding view
     * @param model The route model
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
}
