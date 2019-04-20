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

import edu.uwi.sta.srts.utils.SimpleLocation;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.views.View;

public class ShuttleController extends Controller{
    
    /**
     * Constructor that requires the shuttle model and its corresponding view
     * @param model The shuttle model
     * @param view The corresponding view
     */
    public ShuttleController(Shuttle model, View view) {
        super(model, view);
    }

    public String getShuttleId() {
        return this.model.getId();
    }

    public int getShuttleCapacity() {
        return ((Shuttle)this.model).getCapacity();
    }

    public void setShuttleCapacity(int capacity) {
        ((Shuttle)this.model).setCapacity(capacity);
    }

    public String getShuttleLicensePlateNo() {
        return ((Shuttle)this.model).getLicensePlateNo();
    }

    public String setShuttleLicensePlateNo(String licensePlateNo) {
        return ((Shuttle)this.model).setLicensePlateNo(licensePlateNo);
    }

    public SimpleLocation getShuttleLocation() {
        return ((Shuttle)this.model).getLocation();
    }

    public void setShuttleLocation(SimpleLocation location) {
        ((Shuttle)this.model).setLocation(location);
    }

    public String getShuttleDriverId() {
        return ((Shuttle)this.model).getDriverId();
    }

    public void setShuttleDriverId(String driverId) {
        ((Shuttle)this.model).setDriverId(driverId);
    }

    public String getShuttleRouteId() {
        return ((Shuttle)this.model).getRouteId();
    }

    public void setShuttleRouteId(String routeId) {
        ((Shuttle)this.model).setRouteId(routeId);
    }

    public boolean isShuttleOnDuty() {
        return ((Shuttle)this.model).isOnDuty();
    }

    public void setShuttleOnDuty(boolean onDuty) {
        ((Shuttle)this.model).setOnDuty(onDuty);
    }

    public float getShuttleRotation() {
        return ((Shuttle)this.model).getRotation();
    }

    public void setShuttleRotation(float rotation) {
        ((Shuttle)this.model).setRotation(rotation);
    }

}
