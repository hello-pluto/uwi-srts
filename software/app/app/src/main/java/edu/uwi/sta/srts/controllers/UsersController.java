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

import java.util.ArrayList;

import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.View;

public class UsersController extends Controller {

    /**
     * Constructor that requires the users model and its corresponding view
     * @param model The list of user models
     * @param view The corresponding view
     */
    public UsersController(Users model, View view) {
        super(model, view);
    }

    public ArrayList<User> getUsers() {
        return ((Users)this.model).getUsers();
    }

    /**
     * Method that returns the user controller for a given user
     * @param position The index of the user
     * @param view The view to link the controller to
     * @return A newly created UserController object
     */
    public UserController getUserController(int position, View view){
        return new UserController(getUsers().get(position), view);
    }
}
