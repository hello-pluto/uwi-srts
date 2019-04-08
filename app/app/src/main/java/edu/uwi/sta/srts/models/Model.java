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
 * Interface that abstracts the basic behaviour for all model objects.
 */

package edu.uwi.sta.srts.models;

import com.google.firebase.database.Exclude;

public abstract class Model extends Subject {

    // The model identifier
    protected String id;

    public Model(){
        super();
        this.id = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    /**
     * Method that saves the model i.e. syncs it with the databse
     */
    @Exclude
    public abstract void save();
}
