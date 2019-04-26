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

package edu.uwi.sta.srts.controllers;

import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.UserType;
import edu.uwi.sta.srts.utils.View;

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
}
