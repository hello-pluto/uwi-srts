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

package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.views.View;

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

    public UserController getUserController(int index, View view){
        return new UserController(getUsers().get(index), view);
    }
}
