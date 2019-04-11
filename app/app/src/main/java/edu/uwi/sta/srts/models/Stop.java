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
 * This class represents a stop on a route in the system
 */

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.models.utils.Location;

public class Stop extends Model{

    private String routeId;

    private String name;

    private Location location;

    /**
     * Default constructor for Firebase
     */
    public Stop(){
        super();
        this.name = "";
        this.location = new Location();
    }

    /**
     * Constructor that fetches the stop corresponding to the given stopId
     * @param stopId The stopId of the stop to fetch
     */
    public Stop(String stopId){
        super();
        DatabaseHelper.getInstance().getDatabaseReference("stops").child(stopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Stop s = dataSnapshot.getValue(Stop.class);
                        if(s != null) {
                            Stop.this.setName(s.getName());
                            Stop.this.setRouteId(s.getRouteId());
                            Stop.this.setLocation(s.getLocation());
                            Stop.this.setId(s.getId());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that requires the stop's route id and a location
     * @param routeId The route that this stop belongs to
     * @param location The location of the stop
     */
    public Stop(String routeId, Location location){
        super();
        this.routeId = routeId;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("stops").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("stops")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")) {
            DatabaseHelper.getInstance().getDatabaseReference("stops")
                    .child(getId()).setValue(null);
        }
    }
}
