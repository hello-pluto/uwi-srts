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

package edu.uwi.sta.srts.utils;

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
        if(this.view!=null) {
            this.view.update(model);
        }
    }

    public final void saveModel(){
        this.model.save();
    }

    public final Model getModel() {
        return model;
    }

    public final View getView() {
        return view;
    }
}
