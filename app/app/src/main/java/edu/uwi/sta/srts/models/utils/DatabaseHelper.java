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
 *
 */

package edu.uwi.sta.srts.models.utils;

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

    private DatabaseHelper(){

    }

    public static DatabaseHelper getInstance() {
        if(instance == null){
            instance = new DatabaseHelper();
            database = FirebaseDatabase.getInstance();
        }
        return instance;
    }

    public DatabaseReference getDatabaseReference(String location){
        return database.getReference(location);
    }



    public static void attachIsOnlineListener(final Snackbar offlineSnackbar){
        getInstance().getDatabaseReference(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean online = dataSnapshot.getValue(Boolean.class);
                if(online){
                    offlineSnackbar.dismiss();
                }else{
                    offlineSnackbar.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
