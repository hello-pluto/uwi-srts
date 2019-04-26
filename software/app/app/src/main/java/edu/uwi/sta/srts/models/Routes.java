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

public class Routes extends Model {

    private ArrayList<Route> routes =  new ArrayList<>();

    /**
     * Default constructor that fetches all routes from the database
     */
    public Routes() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("routes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        routes.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            Route r = route.getValue(Route.class);
                            routes.add(r);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public ArrayList<Route> getRoutes() {
        return this.routes;
    }

    @Override
    public void save() {
        for(Route route: this.getRoutes()){
            route.save();
        }
    }

    @Override
    public void delete() {
        for(Route route: this.getRoutes()){
            route.delete();
        }
    }
}
