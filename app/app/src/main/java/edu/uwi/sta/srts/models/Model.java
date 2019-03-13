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

public interface Model {

    /**
     * Method that saves the model i.e. syncs it with the databse
     */
    void save();
}
