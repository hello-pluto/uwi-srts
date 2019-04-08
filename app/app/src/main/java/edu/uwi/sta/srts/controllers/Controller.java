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
 * Interface that abstracts the basic behaviour for all controller objects.
 */

package edu.uwi.sta.srts.controllers;


import android.os.Handler;
import android.util.Log;

import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.views.View;

public abstract class Controller implements Observer{

    protected Model model;

    protected View view;

    public Controller(final Model model, View view){
        this.model = model;
        this.view = view;
        this.model.attachObserver(this);
    }

    @Override
    public void update() {
        this.updateView();
    }

    /**
     * Method that saves the model i.e. sync data with database
     */
    public void saveModel(){
        this.model.save();
    }

    /**
     * Method that updates the view i.e. update view elements with fresh model data
     */
    public void updateView(){
        this.view.update(model);
    }

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }
}
