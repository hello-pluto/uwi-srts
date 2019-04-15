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
 * This class represents a list of shuttles in the system
 */

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

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

                        filterSelf(routeId);

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that accepts a local collection of shuttles
     * @param shuttles The collection of shuttles
     */
    public Shuttles(ArrayList<Shuttle> shuttles){
        super();
        this.shuttles.addAll(shuttles);
    }

    public ArrayList<Shuttle> getShuttles() {
        return this.shuttles;
    }

    public void addShuttle(Shuttle shuttle){
        this.shuttles.add(shuttle);
    }

    public void removeShuttle(int index){
        this.shuttles.remove(index);
    }

    public void filterSelf(String routeId){

        ArrayList<Shuttle> shuttles = new ArrayList<>();

        for(Shuttle shuttle : getShuttles()){
            if(shuttle.getRouteId().equals(routeId)){
                shuttles.add(shuttle);
            }
        }

        this.getShuttles().clear();
        this.getShuttles().addAll(shuttles);
    }

    public Shuttle filter(String driverId){
        for(Shuttle shuttle:getShuttles()){
            if(shuttle.getDriverId().equals(driverId)){
                return shuttle;
            }
        }
        return null;
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
}
