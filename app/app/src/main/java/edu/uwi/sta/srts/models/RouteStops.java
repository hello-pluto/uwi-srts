package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

public class RouteStops extends Model {

    private ArrayList<RouteStop> routeStops =  new ArrayList<>();
    private String filter = null;

    /**
     * Default constructor that fetches all route stops from the database
     */
    public RouteStops() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("routeStops")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        routeStops.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            RouteStop r = route.getValue(RouteStop.class);
                            routeStops.add(r);
                        }

                        if(filter != null){
                            filterSelf(filter);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that accepts a local collection of routes
     * @param routeStops The collection of routes
     */
    public RouteStops(ArrayList<RouteStop> routeStops){
        super();
        this.routeStops.addAll(routeStops);
    }

    public ArrayList<RouteStop> getRouteStops() {
        return this.routeStops;
    }

    public void addRouteStop(RouteStop routeStop){
        this.routeStops.add(routeStop);
    }

    public void removeRouteStop(int index){
        this.routeStops.remove(index);
    }

    public ArrayList<RouteStop> filterSelf(String routeId){

        filter = routeId;

        ArrayList<RouteStop> routeStops = new ArrayList<>();
        for(RouteStop user: getRouteStops()){
            if(user.getRouteId().equals(routeId)){
                routeStops.add(user);
            }
        }

        this.getRouteStops().clear();
        this.getRouteStops().addAll(routeStops);

        return routeStops;
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
