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
 * This abstract class represents the user data in the system
 */

package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.models.utils.UserType;
import edu.uwi.sta.srts.utils.Utils;

public class User extends Model {

    private String email;

    private String fullName;

    private UserType userType;

    private boolean verified;

    /**
     * Default constructor for Firebase
     */
    public User(){
        super();
    }

    /**
     * Constructor that fetches the user corresponding to the given userId
     * @param userId The userId of the user to fetch
     */
    public User(String userId){
        super();
        if(userId.equals("")){
            return;
        }
        DatabaseHelper.getInstance().getDatabaseReference("users").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        if(u != null) {
                            User.this.setEmail(u.getEmail());
                            User.this.setFullName(u.getFullName());
                            User.this.setUserType(u.getUserType());
                            User.this.setVerified(u.isVerified());
                            User.this.setId(u.getId());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that requires a user's email address and full name
     * @param email The user's email address
     * @param fullName The user's full name
     */
    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.userType = UserType.STUDENT;
        this.verified = false;
    }

    /**
     * Constructor that requires a user's email address and full name
     * @param email The user's email address
     * @param fullName The user's full name
     * @param userType The user's account type i.e. Student, Driver or Administrator
     */
    public User(String email, String fullName, UserType userType) {
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
        this.verified = false;
    }

    public String getEmail() {
        return email;
    }

    public String setEmail(String email) {
        if(!Utils.isValidEmail(email)){
            return "Invalid email";
        }
        this.email = email;
        return null;
    }

    public String getFullName() {
        return fullName;
    }

    public String setFullName(String fullName) {
        if(fullName.split(" ").length < 2){
            return "Invalid full name";
        }
        this.fullName = fullName;
        return null;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("users").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("users")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")) {
            DatabaseHelper.getInstance().getDatabaseReference("users")
                    .child(getId()).setValue(null);
        }
    }
}
