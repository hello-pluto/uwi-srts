package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Alert;
import edu.uwi.sta.srts.models.Alerts;
import edu.uwi.sta.srts.views.View;

public class AlertsController extends Controller {


    /**
     * Constructor that requires the alerts model and its corresponding view
     * @param model The list of alerts models
     * @param view The corresponding view
     */
    public AlertsController(Model model, View view) {
        super(model, view);
    }

    public ArrayList<Alert> getAlerts() {
        return ((Alerts)this.model).getAlerts();
    }

    public void addAlert(Alert alert){
        ((Alerts)this.model).addAlert(alert);
    }

    public void removeAlert(int index){
        ((Alerts)this.model).removeAlert(index);
    }

    public AlertController getAlertController(int position, View view){
        return new AlertController(((Alerts)model).getAlerts().get(position), view);
    }


    public Alert filter(String alertId){
        for (Alert alert: ((Alerts)this.model).getAlerts()) {
            if(alert.getId().equals(alertId)){
                return alert;
            }
        }

        return null;
    }


}
