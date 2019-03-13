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

public class StopController implements Controller{

    private Stop model;

    private View view;

    /**
     * Constructor that requires the stop model and its corresponding view
     * @param model The stop model
     * @param view The corresponding view
     */
    public StopController(Stop model, View view) {
        this.model = model;
        this.view = view;
    }

    public String getStopId() {
        return this.model.getStopId();
    }

    public void setStopId(String stopId) {
        this.model.setStopId(stopId);
    }

    public String getStopRouteId() {
        return this.model.getRouteId();
    }

    public void setStopRouteId(String routeId) {
        this.model.setRouteId(routeId);
    }

    public Location getStopLocation() {
        return this.model.getLocation();
    }

    public void setStopLocation(Location location) {
        this.model.setLocation(location);
    }

    @Override
    public void saveModel() {
        this.model.save();
    }

    @Override
    public void updateView() {
        this.view.update();
    }
}
