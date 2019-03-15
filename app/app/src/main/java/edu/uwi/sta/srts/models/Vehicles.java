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
 * This class represents a list of vehicles in the system
 */

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

public class Vehicles extends Model {

    private ArrayList<Vehicle> vehicles =  new ArrayList<>();

    /**
     * Default constructor that fetches all vehicles from the database
     */
    public Vehicles() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("vehicles")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        vehicles.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            Vehicle v = route.getValue(Vehicle.class);
                            vehicles.add(v);
                        }

                        setChanged(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that fetches all vehicles with the corresponding routeId
     * @param routeId The routeId of the vehicles to fetch from the database
     */
    public Vehicles(String routeId){
        super();
        DatabaseHelper.getInstance().getDatabaseReference("vehicles").child("routeId").equalTo(routeId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        vehicles.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            Vehicle v = route.getValue(Vehicle.class);
                            vehicles.add(v);
                        }

                        setChanged(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that accepts a local collection of vehicles
     * @param vehicles The collection of vehicles
     */
    public Vehicles(ArrayList<Vehicle> vehicles){
        super();
        this.vehicles.addAll(vehicles);
    }

    public ArrayList<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public void addVehicle(Vehicle vehicle){
        this.vehicles.add(vehicle);
    }

    public void removeVehicle(int index){
        this.vehicles.remove(index);
    }

    @Override
    public void save() {
        for(Vehicle vehicle: this.getVehicles()){
            vehicle.save();
        }
    }
}
