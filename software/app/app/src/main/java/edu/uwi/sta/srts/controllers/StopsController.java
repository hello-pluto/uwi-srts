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

import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.views.View;

public class StopsController extends Controller{

    /**
     * Constructor that requires the route model and its corresponding view
     * @param model The list of stop models
     * @param view The corresponding view
     */
    public StopsController(Stops model, View view) {
        super(model, view);
    }

    public ArrayList<Stop> getStops() {
        return ((Stops)this.model).getStops();
    }

    public StopController getStopController(int index, View view){
        return new StopController(getStops().get(index), view);
    }
}