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
 * This class acts as a bridge between a single user model and its view.
 */

package edu.uwi.sta.srts.controllers;

import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.utils.UserType;
import edu.uwi.sta.srts.views.View;

public class UserController extends Controller {

    /**
     * Constructor that requires the user model and its corresponding view
     * @param model The user model
     * @param view The corresponding view
     */
    public UserController(User model, View view) {
        super(model, view);
    }

    public String getUserId() {
        return ((User)this.model).getId();
    }

    public void setUserId(String userId) {
        ((User)this.model).setId(userId);
    }

    public String getUserEmail() {
        return ((User)this.model).getEmail();
    }

    public String setUserEmail(String email) {
        return ((User)this.model).setEmail(email);
    }

    public String getUserFullName() {
        return ((User)this.model).getFullName();
    }

    public String setUserFullName(String fullName) {
        return ((User)this.model).setFullName(fullName);
    }

    public UserType getUserType() {
        return ((User)this.model).getUserType();
    }

    public void setUserType(UserType userType) {
        ((User)this.model).setUserType(userType);
    }

    public boolean isUserVerified() {
        return ((User)this.model).isVerified();
    }

    public void setUserVerified(boolean verified) {
        ((User)this.model).setVerified(verified);
    }
}
