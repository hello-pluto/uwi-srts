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

import java.util.ArrayList;

public abstract class Subject {

    private ArrayList<Observer> observers;

    protected Subject(){
        this.observers = new ArrayList<>();
    }

    /**
     * Method that adds a new observer object to the list of observers
     * @param observer The new observer object
     */
    public void attachObserver(Observer observer){
        this.observers.add(observer);
    }

    /**
     * Method that removes an observer object from the list of observers
     * @param observer The observer object to remove
     */
    public void detachObserver(Observer observer){
        this.observers.remove(observer);
    }

    /**
     * Method that notifies all observers that the subject has changed in some way
     */
    public void notifyObservers(){
        for(Observer observer: observers){
            if(observer != null) {
                observer.update();
            }
        }
    }
}
