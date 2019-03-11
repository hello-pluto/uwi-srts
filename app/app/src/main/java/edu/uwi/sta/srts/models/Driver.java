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
 * This class represents a driver user in the system
 */

package edu.uwi.sta.srts.models;

public class Driver extends User {

    private String passwordHash;

    /**
     * Default constructor for Firebase
     */
    public Driver() {

    }

    /**
     * Constructor that requires a user's email and password hash
     * @param email The driver user's email
     * @param passwordHash The driver user's password
     */
    public Driver(String email, String passwordHash) {
        super(email);
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
