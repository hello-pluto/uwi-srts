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

public class UserController implements Controller {

    private User model;

    private View view;

    /**
     * Constructor that requires the user model and its corresponding view
     * @param model The user model
     * @param view The corresponding view
     */
    public UserController(User model, View view) {
        this.model = model;
        this.view = view;
    }

    public String getUserId() {
        return this.model.getUserId();
    }

    public void setUserId(String userId) {
        this.model.setUserId(userId);
    }

    public String getUserEmail() {
        return this.model.getEmail();
    }

    public void setUserEmail(String email) {
        this.model.setEmail(email);
    }

    public String getUserFullName() {
        return this.model.getEmail();
    }

    public void setUserFullName(String fullName) {
        this.model.setFullName(fullName);
    }

    public String getUserPassHash() {
        return this.model.getPassHash();
    }

    public void setUserPassHash(String passHash) {
        this.model.setPassHash(passHash);
    }

    public UserType getUserType() {
        return this.model.getUserType();
    }

    public void setUserType(UserType userType) {
        this.model.setUserType(userType);
    }

    public boolean isUserVerified() {
        return this.model.isVerified();
    }

    public void setUserVerified(boolean verified) {
        this.model.setVerified(verified);
    }

    @Override
    public void saveModel() {
        this.model.save();
    }

    @Override
    public void updateView() {
        this.view.update();
    }
}
