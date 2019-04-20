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

public class Alerts extends Model {

    private ArrayList<Alert> alerts = new ArrayList<>();

    /**
     * Default constructor that fetches all alerts from the database
     */
    public Alerts() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("alerts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        alerts.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            Alert a = route.getValue(Alert.class);
                            alerts.add(a);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public ArrayList<Alert> getAlerts() {
        return alerts;
    }

    @Override
    public void save() {
        for(Alert alert: this.getAlerts()){
            alert.save();
        }
    }

    @Override
    public void delete() {
        for(Alert alert: this.getAlerts()){
            alert.delete();
        }
    }
}
