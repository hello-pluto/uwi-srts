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
 * This class represents a list of routes in the system
 */

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

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

                        setChanged(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that accepts a local collection of routes
     * @param routes The collection of routes
     */
    public Routes(ArrayList<Route> routes){
        super();
        this.routes.addAll(routes);
    }

    public ArrayList<Route> getRoutes() {
        return this.routes;
    }

    public void addRoute(Route route){
        this.routes.add(route);
    }

    public void removeRoute(int index){
        this.routes.remove(index);
    }

    @Override
    public void save() {
        for(Route route: this.getRoutes()){
            route.save();
        }
    }
}
