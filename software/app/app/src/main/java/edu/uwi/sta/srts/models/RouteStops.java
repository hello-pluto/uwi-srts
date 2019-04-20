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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.utils.DatabaseHelper;

public class RouteStops extends Model {

    private ArrayList<RouteStop> routeStops =  new ArrayList<>();

    private String filter = null;

    /**
     * Default constructor that fetches all route stops from the database
     */
    public RouteStops() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("routeStops").orderByChild("order")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        routeStops.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            RouteStop r = route.getValue(RouteStop.class);
                            routeStops.add(r);
                        }

                        if(filter != null){
                            filter(filter);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Method that filters the list of route stops for a given route
     * @param routeId The id of the route to getAlert route stops for
     */
    public void filter(String routeId){

        filter = routeId;

        ArrayList<RouteStop> routeStops = new ArrayList<>();
        for(RouteStop user: getRouteStops()){
            if(user.getRouteId().equals(routeId)){
                routeStops.add(user);
            }
        }

        this.getRouteStops().clear();
        this.getRouteStops().addAll(routeStops);
    }

    public ArrayList<RouteStop> getRouteStops() {
        return this.routeStops;
    }

    @Override
    public void save() {
        for(RouteStop routeStop: this.getRouteStops()){
            routeStop.save();
        }
    }

    @Override
    public void delete() {
        for(RouteStop routeStop: this.getRouteStops()){
            routeStop.delete();
        }
    }
}
