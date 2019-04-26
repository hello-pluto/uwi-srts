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

import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.Alert;
import edu.uwi.sta.srts.models.Alerts;
import edu.uwi.sta.srts.utils.View;

public class AlertsController extends Controller {

    /**
     * Constructor that requires the alerts model and its corresponding view
     * @param model The list of alert models
     * @param view The corresponding view
     */
    public AlertsController(Model model, View view) {
        super(model, view);
    }

    public ArrayList<Alert> getAlerts() {
        return ((Alerts)this.model).getAlerts();
    }

    /**
     * Method that returns the alert controller for a given alert
     * @param position The index of the alert
     * @param view The view to link the controller to
     * @return A newly created AlertController object
     */
    public AlertController getAlertController(int position, View view){
        return new AlertController(((Alerts)model).getAlerts().get(position), view);
    }
}
