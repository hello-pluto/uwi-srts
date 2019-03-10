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

public abstract class User {

    private String userId;

    private String email;

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
