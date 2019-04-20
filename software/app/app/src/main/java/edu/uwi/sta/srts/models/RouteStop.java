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

public class RouteStop extends Model {

    private int order;

    private String routeId;

    private String stopId;

    /**
     * Default constructor
     */
    public RouteStop(){
        super();
    }

    /**
     * Constructor that fetches the route stop corresponding to the given routeStopId
     * @param routeStopId The id of the route stop to fetch
     */
    public RouteStop(String routeStopId){
        super();
        if(routeStopId == null || routeStopId.equals("")){
            return;
        }
        DatabaseHelper.getInstance().getDatabaseReference("routeStop").child(routeStopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RouteStop s = dataSnapshot.getValue(RouteStop.class);
                        if(s != null) {
                            RouteStop.this.setRouteId(s.getRouteId());
                            RouteStop.this.setStopId(s.getStopId());
                            RouteStop.this.setOrder(s.getOrder());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that creates a new route stop given its order and corresponding route and stop ids
     * @param order The order of the stop on the route
     * @param routeId The corresponding route's id
     * @param stopId The corresponding stop's id
     */
    public RouteStop(int order, String routeId, String stopId) {
        this.order = order;
        this.routeId = routeId;
        this.stopId = stopId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("routeStops").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("routeStops")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")){
            DatabaseHelper.getInstance().getDatabaseReference("routeStops")
                    .child(getId()).setValue(null);
        }
    }
}