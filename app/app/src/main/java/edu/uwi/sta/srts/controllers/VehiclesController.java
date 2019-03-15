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
 * This class acts as a bridge between a list of route vehicles and their view.
 */

package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.Vehicle;
import edu.uwi.sta.srts.models.Vehicles;
import edu.uwi.sta.srts.views.View;

public class VehiclesController extends Controller{

    /**
     * Constructor that requires the vehicle model and its corresponding view
     * @param model The list of vehicle models
     * @param view The corresponding view
     */
    public VehiclesController(Vehicles model, View view) {
        super(model, view);
    }

    public ArrayList<Vehicle> getModels() {
        return ((Vehicles)this.model).getVehicles();
    }

    public void addVehicle(Vehicle vehicle){
        ((Vehicles)this.model).addVehicle(vehicle);
    }

    public void removeVehicle(int index){
        ((Vehicles)this.model).removeVehicle(index);
    }
}