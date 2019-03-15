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

import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.models.utils.UserType;

public class User extends Model{

    private String email;

    private String fullName;

    private String passHash;

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
        DatabaseHelper.getInstance().getDatabaseReference("users").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        if(u != null) {
                            User.this.setEmail(u.getEmail());
                            User.this.setFullName(u.getFullName());
                            User.this.setPassHash(u.getPassHash());
                            User.this.setUserType(u.getUserType());
                            User.this.setVerified(u.isVerified());
                            User.this.setId(u.getId());

                            setChanged(true);
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
     * @param passHash The user's password hash
     * @param userType The user's account type i.e. Student, Driver or Administrator
     */
    public User(String email, String fullName, String passHash, UserType userType) {
        this.email = email;
        this.fullName = fullName;
        this.passHash = passHash;
        this.userType = userType;
        this.verified = false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
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
}
