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
 * This class represents a shuttle in the system
 */

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.models.utils.Location;

public class Shuttle extends Model{

    private int capacity;

    private String licensePlateNo;

    private Location location;

    private String driverId;

    private String routeId;

    private boolean onDuty;

    private float rotation;

    /**
     * Default constructor for Firebase
     */
    public Shuttle() {
        super();
        this.location = new Location();
        this.driverId = "";
        this.routeId = "";
        this.onDuty = false;
        this.rotation = 0;
    }

    /**
     * Constructor that fetches the shuttle corresponding to the given shuttleId
     * @param shuttleId The shuttleId of the shuttle to fetch from the database
     */
    public Shuttle(String shuttleId){
        super();
        if(shuttleId.equals("")){
            return;
        }
        DatabaseHelper.getInstance().getDatabaseReference("shuttles").child(shuttleId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Shuttle v = dataSnapshot.getValue(Shuttle.class);
                        if(v != null) {
                            Shuttle.this.setCapacity(v.getCapacity());
                            Shuttle.this.setLicensePlateNo(v.getLicensePlateNo());
                            Shuttle.this.setLocation(new Location(v.getLocation().getLatitude(), v.getLocation().getLongitude()));
                            Shuttle.this.setDriverId(v.getDriverId());
                            Shuttle.this.setRouteId(v.getRouteId());
                            Shuttle.this.setOnDuty(v.isOnDuty());
                            Shuttle.this.setRotation(v.getRotation());
                            Shuttle.this.setId(v.getId());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that requires the number of capacity, licence plate number of a shuttle
     * @param capacity The maximum amount of passengers that the shuttle can hold e.g. 12
     * @param licensePlateNo The licence plate number of the shuttle
     */
    public Shuttle(int capacity, String licensePlateNo) {
        this();
        this.capacity = capacity;
        this.licensePlateNo = licensePlateNo;
        this.location = new Location();
        this.driverId = "";
        this.routeId = "";
    }

    /**
     * Constructor that requires the number of capacity, licence plate number, location, driverId
     * and routeId of a shuttle
     * @param capacity The maximum amount of passengers that the shuttle can hold e.g. 12
     * @param licensePlateNo The licence plate number of the shuttle
     * @param driverId The driver that the shuttle is currently assigned to
     * @param routeId The route that the shuttle is currently taking
     */
    public Shuttle(int capacity, String licensePlateNo, String driverId, String routeId) {
        this();
        this.capacity = capacity;
        this.licensePlateNo = licensePlateNo;
        this.location = new Location();
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

    public boolean isOnDuty() {
        return onDuty;
    }

    public void setOnDuty(boolean onDuty) {
        this.onDuty = onDuty;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("shuttles").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("shuttles")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")) {
            DatabaseHelper.getInstance().getDatabaseReference("shuttles")
                    .child(getId()).setValue(null);
        }
    }
}
