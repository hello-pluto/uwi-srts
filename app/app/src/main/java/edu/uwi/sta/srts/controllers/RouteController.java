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

public class RouteController implements Controller{

    private Route model;

    private View view;

    /**
     * Constructor that requires the route model and its corresponding view
     * @param model The route model
     * @param view The corresponding view
     */
    public RouteController(Route model, View view) {
        this.model = model;
        this.view = view;
    }

    public String getRouteId() {
        return this.model.getRouteId();
    }

    public void setRouteId(String routeId) {
        this.model.setRouteId(routeId);
    }

    public String getRouteName() {
        return this.model.getName();
    }

    public void setRouteName(String name) {
        this.model.setName(name);
    }

    public int getRouteFrequency() {
        return this.model.getFrequency();
    }

    public void setRouteFrequency(int frequency) {
        this.model.setFrequency(frequency);
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
