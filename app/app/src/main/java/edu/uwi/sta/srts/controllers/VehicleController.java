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
 * This class acts as a bridge between a single vehicle model and its view.
 */

package edu.uwi.sta.srts.controllers;

import edu.uwi.sta.srts.models.utils.Location;
import edu.uwi.sta.srts.models.Vehicle;
import edu.uwi.sta.srts.views.View;

public class VehicleController implements Controller{

    private Vehicle model;

    private View view;

    /**
     * Constructor that requires the vehicle model and its corresponding view
     * @param model The vehicle model
     * @param view The corresponding view
     */
    public VehicleController(Vehicle model, View view) {
        this.model = model;
        this.view = view;
    }

    public String getVehicleId() {
        return this.model.getVehicleId();
    }

    public void setVehicleId(String vehicleId) {
        this.model.setVehicleId(vehicleId);
    }

    public int getVehicleCapacity() {
        return this.model.getCapacity();
    }

    public void setVehicleCapacity(int capacity) {
        this.model.setCapacity(capacity);
    }

    public String getVehicleLicensePlateNo() {
        return this.model.getLicensePlateNo();
    }

    public void setVehicleLicensePlateNo(String licensePlateNo) {
        this.model.setLicensePlateNo(licensePlateNo);
    }

    public Location getVehicleLocation() {
        return this.model.getLocation();
    }

    public void setVehicleLocation(Location location) {
        this.model.setLocation(location);
    }

    public String getVehicleDriverId() {
        return this.model.getDriverId();
    }

    public void setVehicleDriverId(String driverId) {
        this.model.setDriverId(driverId);
    }

    public String getVehicleRouteId() {
        return this.model.getRouteId();
    }

    public void setVehicleRouteId(String routeId) {
        this.model.setRouteId(routeId);
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
