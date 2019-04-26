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

import edu.uwi.sta.srts.models.Alert;
import edu.uwi.sta.srts.utils.Controller;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.utils.View;

public class AlertController extends Controller {

    /**
     * Constructor that requires the alert model and its corresponding view
     * @param model The alert model
     * @param view The corresponding view
     */
    public AlertController(Model model, View view) {
        super(model, view);
    }

    public String getAlertId() {
        return this.model.getId();
    }

    public String getAlertTitle() {
        return ((Alert)this.model).getTitle();
    }

    public void setAlertTitle(String title) {
        ((Alert)this.model).setTitle(title);
    }

    public String getAlertMessage() {
        return (((Alert)this.model)).getMessage();
    }

    public void setAlertMessage(String name) {
        ((Alert)this.model).setMessage(name);
    }

    public int getAlertUrgency() {
        return ((Alert)this.model).getUrgency();
    }

    public void setAlertUrgency(int urgency) {
        ((Alert)this.model).setUrgency(urgency);
    }

}
