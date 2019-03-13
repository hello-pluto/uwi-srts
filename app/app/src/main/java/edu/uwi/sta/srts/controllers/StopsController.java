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
 * This class acts as a bridge between a list of stop models and their view.
 */

package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.views.View;

public class StopsController implements Controller{

    private Stops model;

    private View view;

    /**
     * Constructor that requires the route model and its corresponding view
     * @param model The list of stop models
     * @param view The corresponding view
     */
    public StopsController(Stops model, View view) {
        this.model = model;
        this.view = view;
    }

    public ArrayList<Stop> getStops() {
        return this.model.getStops();
    }

    public void addStop(Stop stop){
        this.model.addStop(stop);
    }

    public void removeStop(Stop stop){
        this.model.removeStop(stop);
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