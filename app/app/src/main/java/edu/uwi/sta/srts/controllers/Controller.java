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

public interface Controller {

    /**
     * Method that saves the model i.e. sync data with database
     */
    void saveModel();

    /**
     * Method that updates the view i.e. update view elements with fresh model data
     */
    void updateView();
}
