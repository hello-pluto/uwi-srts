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

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.utils.DatabaseHelper;
import edu.uwi.sta.srts.utils.SimpleLocation;

public class Shuttle extends Model{

    private int capacity;

    private String licensePlateNo;

    private SimpleLocation location;

    private String driverId;

    private String routeId;

    private boolean onDuty;

    private float rotation;

    /**
     * Default constructor
     */
    public Shuttle() {
        super();
        this.location = new SimpleLocation();
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
        if(shuttleId == null || shuttleId.equals("")){
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
                            Shuttle.this.setLocation(new SimpleLocation(v.getLocation().getLatitude(), v.getLocation().getLongitude()));
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

    public SimpleLocation getLocation() {
        return location;
    }

    public void setLocation(SimpleLocation location) {
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
