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

package edu.uwi.sta.srts.views;

import edu.uwi.sta.srts.models.Model;

public interface View {

    /**
     * Method that allows view objects to update their state based on fresh model data
     * @param model The model to update the view with
     */
    void update(Model model);
}
