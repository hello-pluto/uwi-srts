/*
 * Copyright (c) 2019. Razor Sharp Software Solutions
 *
 * Azel Daniel (816002285)
 * Michael Bristol (816003612)
 * Amanda Seenath (816002935)
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
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.utils.SimpleLocation;

public class Stop extends Model {

    private String routeId;
    private String name;
    private SimpleLocation location;

    /**
     * Default constructor
     */
    public Stop(){
        super();
        this.name = "";
        this.location = new SimpleLocation();
    }

    /**
     * Constructor that fetches the stop corresponding to the given stop id
     * @param stopId The stop id of the stop to fetch
     */
    public Stop(String stopId){
        super();
        if(stopId == null || stopId.equals("")){
            return;
        }
        DatabaseHelper.getInstance().getDatabaseReference("stops").child(stopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Stop s = dataSnapshot.getValue(Stop.class);
                        if(s != null) {
                            Stop.this.setName(s.getName());
                            Stop.this.setRouteId(s.getRouteId());
                            Stop.this.setLocation(new SimpleLocation(s.getLocation().getLatitude(), s.getLocation().getLongitude()));
                            Stop.this.setId(s.getId());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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

    public SimpleLocation getLocation() {
        return location;
    }

    public void setLocation(SimpleLocation location) {
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
