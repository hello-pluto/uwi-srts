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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.utils.DatabaseHelper;
import edu.uwi.sta.srts.utils.Model;

public class Shuttles extends Model {

    private ArrayList<Shuttle> shuttles =  new ArrayList<>();

    /**
     * Default constructor that fetches all shuttles from the database
     */
    public Shuttles() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("shuttles")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shuttles.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            Shuttle v = route.getValue(Shuttle.class);
                            shuttles.add(v);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that fetches all shuttles with the corresponding routeId
     * @param routeId The routeId of the shuttles to fetch from the database
     */
    public Shuttles(final String routeId){
        super();
        DatabaseHelper.getInstance().getDatabaseReference("shuttles")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shuttles.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            Shuttle v = route.getValue(Shuttle.class);
                            shuttles.add(v);
                        }

                        filter(routeId);

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Method that filters the list of shuttles for a given route id
     * @param routeId The id of the route to get shuttles for
     */
    public void filter(String routeId){

        ArrayList<Shuttle> shuttles = new ArrayList<>();

        for(Shuttle shuttle : getShuttles()){
            if(shuttle.getRouteId().equals(routeId)){
                shuttles.add(shuttle);
            }
        }

        this.getShuttles().clear();
        this.getShuttles().addAll(shuttles);
    }

    public ArrayList<Shuttle> getShuttles() {
        return this.shuttles;
    }

    @Override
    public void save() {
        for(Shuttle shuttle: this.getShuttles()){
            shuttle.save();
        }
    }

    @Override
    public void delete() {
        for(Shuttle shuttle: this.getShuttles()){
            shuttle.delete();
        }
    }

    /**
     * Method that filters the list of shuttles for a given on duty status
     * @param onDuty Whether to filter by shuttles that are on duty or not
     */
    public void filter(boolean onDuty){
        ArrayList<Shuttle> shuttles = new ArrayList<>();

        for(Shuttle shuttle : getShuttles()){
            if(shuttle.isOnDuty() == onDuty){
                shuttles.add(shuttle);
            }
        }

        this.getShuttles().clear();
        this.getShuttles().addAll(shuttles);
    }
}
