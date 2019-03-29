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

public class VehicleController extends Controller{
    
    /**
     * Constructor that requires the vehicle model and its corresponding view
     * @param model The vehicle model
     * @param view The corresponding view
     */
    public VehicleController(Vehicle model, View view) {
        super(model, view);
    }

    public String getVehicleId() {
        return ((Vehicle)this.model).getId();
    }

    public int getVehicleCapacity() {
        return ((Vehicle)this.model).getCapacity();
    }

    public void setVehicleCapacity(int capacity) {
        ((Vehicle)this.model).setCapacity(capacity);
    }

    public String getVehicleLicensePlateNo() {
        return ((Vehicle)this.model).getLicensePlateNo();
    }

    public void setVehicleLicensePlateNo(String licensePlateNo) {
        ((Vehicle)this.model).setLicensePlateNo(licensePlateNo);
    }

    public Location getVehicleLocation() {
        return ((Vehicle)this.model).getLocation();
    }

    public void setVehicleLocation(Location location) {
        ((Vehicle)this.model).setLocation(location);
    }

    public String getVehicleDriverId() {
        return ((Vehicle)this.model).getDriverId();
    }

    public void setVehicleDriverId(String driverId) {
        ((Vehicle)this.model).setDriverId(driverId);
    }

    public String getVehicleRouteId() {
        return ((Vehicle)this.model).getRouteId();
    }

    public void setVehicleRouteId(String routeId) {
        ((Vehicle)this.model).setRouteId(routeId);
    }
}
