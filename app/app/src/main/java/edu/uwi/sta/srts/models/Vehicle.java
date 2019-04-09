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

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.models.utils.Location;

public class Vehicle extends Model{

    private int capacity;

    private String licensePlateNo;

    private Location location;

    private String driverId;

    private String routeId;

    /**
     * Default constructor for Firebase
     */
    public Vehicle() {
        super();
    }

    /**
     * Constructor that fetches the vehicle corresponding to the given vehicleId
     * @param vehicleId The vehicleId of the vehicle to fetch from the database
     */
    public Vehicle(String vehicleId){
        super();
        DatabaseHelper.getInstance().getDatabaseReference("vehicles").child(vehicleId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Vehicle v = dataSnapshot.getValue(Vehicle.class);
                        if(v != null) {
                            Vehicle.this.setCapacity(v.getCapacity());
                            Vehicle.this.setLicensePlateNo(v.getLicensePlateNo());
                            Vehicle.this.setLocation(v.getLocation());
                            Vehicle.this.setDriverId(v.getDriverId());
                            Vehicle.this.setRouteId(v.getRouteId());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that requires the number of capacity, licence plate number of a vehicle
     * @param capacity The maximum amount of passengers that the vehicle can hold e.g. 12
     * @param licensePlateNo The licence plate number of the vehicle
     */
    public Vehicle(int capacity, String licensePlateNo) {
        super();
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
        super();
        this.capacity = capacity;
        this.licensePlateNo = licensePlateNo;
        this.location = null;
        this.driverId = driverId;
        this.routeId = routeId;
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

    public String setLicensePlateNo(String licensePlateNo) {
        if(licensePlateNo.length() >= 4 && licensePlateNo.length() <= 7 && Character.isLetter(licensePlateNo.charAt(0)) &&
                Character.isLetter(licensePlateNo.charAt(1)) &&
                Character.isLetter(licensePlateNo.charAt(2)) &&
                Character.isDigit(licensePlateNo.charAt(3))){
            this.licensePlateNo = licensePlateNo;
        }else {

            return "Invalid Plate Number";
        }
        return null;
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
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("vehicles").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("vehicles")
                    .child(getId()).setValue(this);
        }
    }
}
