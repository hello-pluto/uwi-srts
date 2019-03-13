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

public class RoutesController implements Controller{

    private Routes model;

    private View view;

    /**
     * Constructor that requires the routes model and its corresponding view
     * @param model The list of route models
     * @param view The corresponding view
     */
    public RoutesController(Routes model, View view) {
        this.model = model;
        this.view = view;
    }

    public ArrayList<Route> getRoutes() {
        return this.model.getRoutes();
    }

    public void addRoute(Route route){
        this.model.addRoute(route);
    }

    public void removeRoute(Route route){
        this.model.removeRoute(route);
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