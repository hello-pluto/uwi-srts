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

public class Route extends Model {

    private String name;
    private int frequency;

    /**
     * Default constructor
     */
    public Route(){
        super();
    }

    /**
     * Constructor that fetches the route corresponding to the given route id
     * @param routeId The route id of the route to fetch
     */
    public Route(String routeId){
        super();
        if(routeId == null || routeId.equals("")){
            return;
        }
        DatabaseHelper.getInstance().getDatabaseReference("routes").child(routeId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Route r = dataSnapshot.getValue(Route.class);
                        if(r != null) {
                            Route.this.setName(r.getName());
                            Route.this.setFrequency(r.getFrequency());
                            Route.this.setId(r.getId());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that create a new route object and requires a route name and frequency
     * @param name The name of the route e.g. JFK-SAL
     * @param frequency The number of minutes between each shuttle arrival e.g. 15 (minutes)
     */
    public Route(String name, int frequency){
        super();
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("routes").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("routes")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")) {
            DatabaseHelper.getInstance().getDatabaseReference("routes")
                    .child(getId()).setValue(null);
        }
    }
}
