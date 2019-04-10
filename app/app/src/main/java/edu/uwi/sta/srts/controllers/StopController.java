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
 * This class acts as a bridge between a single stop model and its view.
 */

package edu.uwi.sta.srts.controllers;

import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.utils.Location;
import edu.uwi.sta.srts.views.View;

public class StopController extends Controller{

    /**
     * Constructor that requires the stop model and its corresponding view
     * @param model The stop model
     * @param view The corresponding view
     */
    public StopController(Stop model, View view) {
        super(model, view);
    }

    public String getStopName() {
        return ((Stop)this.model).getName();
    }

    public void setStopName(String name) {
        ((Stop)this.model).setName(name);
    }

    public String getStopId() {
        return ((Stop)this.model).getId();
    }

    public String getStopRouteId() {
        return ((Stop)this.model).getRouteId();
    }

    public void setStopRouteId(String routeId) {
        ((Stop)this.model).setRouteId(routeId);
    }

    public Location getStopLocation() {
        return ((Stop)this.model).getLocation();
    }

    public void setStopLocation(Location location) {
        ((Stop)this.model).setLocation(location);
    }

}
