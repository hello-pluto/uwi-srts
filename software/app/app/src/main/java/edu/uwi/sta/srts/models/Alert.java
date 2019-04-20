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

public class Alert extends Model {

    private String title;

    private String message;

    private int urgency;

    /**
     * Default constructor
     */
    public Alert () {
        super();
    }

    /**
     * Constructor that fetches the alert corresponding to the given alertId
     * @param alertId The alertId of the alert to fetch
     */
    public Alert(String alertId){
        super();
        if(alertId == null || alertId.equals("")){
            return;
        }
        DatabaseHelper.getInstance().getDatabaseReference("alerts").child(alertId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Alert a = dataSnapshot.getValue(Alert.class);
                        if(a != null) {
                            Alert.this.setTitle(a.getTitle());
                            Alert.this.setMessage(a.getMessage());
                            Alert.this.setId(a.getId());
                            Alert.this.setUrgency(a.getUrgency());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that creates a new alert object given a title, message and urgency
     * @param title The summary of the alert
     * @param message The details of the alert
     * @param urgency The level of urgency of the alert; ranges from 1-5, where 5 is most urgent
     */
    public Alert(String title, String message, int urgency) {
        this.title = title;
        this.message = message;
        this.urgency = urgency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("alerts").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("alerts")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")) {
            DatabaseHelper.getInstance().getDatabaseReference("alerts")
                    .child(getId()).setValue(null);
        }
    }
}
