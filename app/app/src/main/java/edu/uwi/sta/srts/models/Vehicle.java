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
 * This class represents a vehicle in the system
 */

package edu.uwi.sta.srts.models;

import edu.uwi.sta.srts.models.utils.Location;

public class Vehicle implements Model{

    private String vehicleId;

    private int capacity;

    private String licensePlateNo;

    private Location location;

    private String driverId;

    private String routeId;

    /**
     * Default constructor for Firebase
     */
    public Vehicle() {

    }

    /**
     * Constructor that fetches the vehicle corresponding to the given vehicleId
     * @param vehicleId The vehicleId of the vehicle to fetch from the database
     */
    public Vehicle(String vehicleId){
        // TODO: Fetch vehicle from database corresponding to vehicleId
    }

    /**
     * Constructor that requires the number of capacity, licence plate number of a vehicle
     * @param capacity The maximum amount of passengers that the vehicle can hold e.g. 12
     * @param licensePlateNo The licence plate number of the vehicle
     */
    public Vehicle(int capacity, String licensePlateNo) {
        this.capacity = capacity;
        this.licensePlateNo = licensePlateNo;
        this.location = null;
        this.driverId = null;
        this.routeId = null;
    }

    /**
     * Constructor that requires the number of capacity, licence plate number, location, driverId
     * and routeId of a vehicle
     * @param capacity The maximum amount of passengers that the vehicle can hold e.g. 12
     * @param licensePlateNo The licence plate number of the vehicle
     * @param driverId The driver that the vehicle is currently assigned to
     * @param routeId The route that the vehicle is currently taking
     */
    public Vehicle(int capacity, String licensePlateNo, String driverId, String routeId) {
        this.capacity = capacity;
        this.licensePlateNo = licensePlateNo;
        this.location = null;
        this.driverId = driverId;
        this.routeId = routeId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public int hashCode() {
        return this.getVehicleId().hashCode();
    }

    @Override
    public void save() {
        // TODO: sync with database
    }
}
