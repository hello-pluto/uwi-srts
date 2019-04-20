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
import edu.uwi.sta.srts.utils.UserType;

public class Users extends Model {

    private ArrayList<User> users =  new ArrayList<>();

    private UserType filter = null;

    /**
     * Default constructor that fetches all users from the database
     */
    public Users() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot user: dataSnapshot.getChildren()) {
                            User u = user.getValue(User.class);
                            if(filter != null && u != null && u.getUserType() == filter){
                                users.add(u);
                            }
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public ArrayList<User> filter(UserType userType){

        filter = userType;

        ArrayList<User> users = new ArrayList<>();
        for(User user: getUsers()){
            if(user.getUserType() == userType){
                users.add(user);
            }
        }

        this.users.clear();
        this.users.addAll(users);

        return users;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    @Override
    public void save() {
        for(User user: this.getUsers()){
            user.save();
        }
    }

    @Override
    public void delete() {
        for(User user: this.getUsers()){
            user.delete();
        }
    }
}
