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

import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.View;

public class ShuttlesController extends Controller {

    /**
     * Constructor that requires the shuttle model and its corresponding view
     * @param model The list of shuttle models
     * @param view The corresponding view
     */
    public ShuttlesController(Shuttles model, View view) {
        super(model, view);
    }

    public ArrayList<Shuttle> getShuttles() {
        return ((Shuttles)this.model).getShuttles();
    }

    /**
     * Method that returns the shuttle controller for a given shuttle
     * @param position The index of the shuttle
     * @param view The view to link the controller to
     * @return A newly created ShuttleController object
     */
    public ShuttleController getShuttleController(int position, View view){
        return new ShuttleController(getShuttles().get(position), view);
    }
}