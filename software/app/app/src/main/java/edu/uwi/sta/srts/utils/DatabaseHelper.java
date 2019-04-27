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

package edu.uwi.sta.srts.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHelper {

    private static DatabaseHelper instance;
    private static FirebaseDatabase database;

    /**
     * Private singleton constructor
     */
    private DatabaseHelper(){}

    public static DatabaseHelper getInstance() {
        if(instance == null){
            instance = new DatabaseHelper();
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return instance;
    }

    /**
     * Method that returns the Firebase database reference for a given location in the database
     * @param location The path in the database to get the reference for
     * @return A DatabaseReference object at the given location
     */
    public DatabaseReference getDatabaseReference(String location){
        return database.getReference(location);
    }

    /**
     * Method that shows a Snackbar whenever the internet connection is not available to the application
     * @param offlineSnackbar The Snackbar notification to show when offline
     */
    public static void attachIsOnlineListener(final Snackbar offlineSnackbar){
        getInstance().getDatabaseReference(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    boolean online = dataSnapshot.getValue(Boolean.class);
                    if (online) {
                        offlineSnackbar.dismiss();
                    } else {
                        offlineSnackbar.show();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
