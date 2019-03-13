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
 * This class represents a list of vehicles in the system
 */

package edu.uwi.sta.srts.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Vehicles implements Model {

    private HashMap<String, Vehicle> vehicles =  new HashMap<>();

    /**
     * Default constructor that fetches all vehicles from the database
     */
    public Vehicles() {
        // TODO: Fetch data for all vehicles from the database.
    }

    /**
     * Constructor that fetches all vehicles with the corresponding routeId
     * @param routeId The routeId of the vehicles to fetch from the database
     */
    public Vehicles(String routeId){
        // TODO: Fetch data from database about all vehicles on this route
    }

    /**
     * Constructor that accepts a local collection of vehicles
     * @param vehicles The collection of vehicles
     */
    public Vehicles(HashMap<String, Vehicle> vehicles){
        this.vehicles.putAll(vehicles);
    }

    public ArrayList<Vehicle> getVehicles() {
        return new ArrayList<Vehicle>(this.vehicles.values());
    }

    public void addVehicle(Vehicle vehicle){
        this.vehicles.put(vehicle.getVehicleId(), vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
        this.vehicles.remove(vehicle.getVehicleId());
    }

    @Override
    public int hashCode() {
        return this.vehicles.hashCode();
    }

    @Override
    public void save() {
        // TODO: sync with database
    }
}
