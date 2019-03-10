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

public class Vehicle {

    private String vehicleId;

    private int seats;

    private String licencePlate;

    private Location location;

    private String driverId;

    private String routeId;

    /**
     * Default constructor for Firebase
     */
    public Vehicle() {

    }

    /**
     * Constructor that requires the number of seats, licence plate number of a vehicle
     * @param seats The maximum amount of passengers that the vehicle can hold e.g. 12
     * @param licencePlate The licence plate number of the vehicle
     */
    public Vehicle(int seats, String licencePlate) {
        this.seats = seats;
        this.licencePlate = licencePlate;
        this.location = null;
        this.driverId = null;
        this.routeId = null;
    }

    /**
     * Constructor that requires the number of seats, licence plate number, location, driverId
     * and routeId of a vehicle
     * @param seats The maximum amount of passengers that the vehicle can hold e.g. 12
     * @param licencePlate The licence plate number of the vehicle
     * @param driverId The driver that the vehicle is currently assigned to
     * @param routeId The route that the vehicle is currently taking
     */
    public Vehicle(int seats, String licencePlate, String driverId, String routeId) {
        this.seats = seats;
        this.licencePlate = licencePlate;
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

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
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
}
