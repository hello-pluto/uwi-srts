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

import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.views.View;

public class ShuttlesController extends Controller{

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

    public ShuttleController getShuttleController(int index, View view){
        return new ShuttleController(getShuttles().get(index), view);
    }

    public Shuttle getShuttle(String shuttleId){
        for(Shuttle shuttle: getShuttles()){
            if(shuttle.getId().equals(shuttleId)){
                return shuttle;
            }
        }
        return null;
    }
}