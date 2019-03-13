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

import edu.uwi.sta.srts.models.utils.UserType;

public class User implements Model{

    private String userId;

    private String email;

    private String fullName;

    private String passHash;

    private UserType userType;

    private boolean verified;

    /**
     * Default constructor for Firebase
     */
    public User(){

    }

    /**
     * Constructor that fetches the user corresponding to the given userId
     * @param userId The userId of the user to fetch
     */
    public User(String userId){
        // TODO: Fetch user from database corresponding to userId
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    public int hashCode() {
        return this.getUserId().hashCode();
    }

    @Override
    public void save() {
        // TODO: Sync data with database
    }
}
