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

import java.util.ArrayList;

import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.View;

public class StopsController extends Controller {

    /**
     * Constructor that requires the stops model and its corresponding view
     * @param model The list of stop models
     * @param view The corresponding view
     */
    public StopsController(Stops model, View view) {
        super(model, view);
    }

    public ArrayList<Stop> getStops() {
        return ((Stops)this.model).getStops();
    }

    /**
     * Method that returns the stop controller for a given stop
     * @param position The index of the stop
     * @param view The view to link the controller to
     * @return A newly created StopController object
     */
    public StopController getStopController(int position, View view){
        return new StopController(getStops().get(position), view);
    }
}