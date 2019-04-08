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
 * This class represents a list of stops in the system
 */

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

public class Stops extends Model {

    private ArrayList<Stop> stops =  new ArrayList<>();

    /**
     * Default constructor that fetches all stops from the database
     */
    public Stops() {
        DatabaseHelper.getInstance().getDatabaseReference("stops")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stops.clear();
                        for (DataSnapshot stop: dataSnapshot.getChildren()) {
                            Stop r = stop.getValue(Stop.class);
                            stops.add(r);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that fetches all stops with the corresponding routeId
     * @param routeId The routeId of the stops to fetch from the database
     */
    public Stops(String routeId){
        DatabaseHelper.getInstance().getDatabaseReference("routeStops").child("routeId").equalTo(routeId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stops.clear();
                        for (DataSnapshot stop: dataSnapshot.getChildren()) {
                            Stop r = stop.getValue(Stop.class);
                            stops.add(r);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that accepts a local collection of stops
     * @param stops The collection of local stops
     */
    public Stops(ArrayList<Stop> stops){
        this.stops.addAll(stops);
    }

    public ArrayList<Stop> getStops() {
        return this.stops;
    }

    public void addStop(Stop stop){
        this.stops.add(stop);
    }

    public void removeStop(int index){
        this.stops.remove(index);
    }

    @Override
    public void save() {
        for(Stop stop: this.getStops()){
            stop.save();
        }
    }
}
