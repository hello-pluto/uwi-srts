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

public class VehiclesController implements Controller{

    private Vehicles model;

    private View view;

    /**
     * Constructor that requires the vehicle model and its corresponding view
     * @param model The list of vehicle models
     * @param view The corresponding view
     */
    public VehiclesController(Vehicles model, View view) {
        this.model = model;
        this.view = view;
    }

    public ArrayList<Vehicle> getModels() {
        return this.model.getVehicles();
    }

    public void addVehicle(Vehicle vehicle){
        this.model.addVehicle(vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
        this.model.removeVehicle(vehicle);
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