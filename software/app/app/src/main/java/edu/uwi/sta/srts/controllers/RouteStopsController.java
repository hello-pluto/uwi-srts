package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.views.View;

public class RouteStopsController extends Controller{

    private String filter;

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

    public void addRouteStop(RouteStop route){
        ((RouteStops)this.model).addRouteStop(route);
    }

    public void removeRoute(int index){
        ((RouteStops)this.model).removeRouteStop(index);
    }
}
