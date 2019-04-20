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

package edu.uwi.sta.srts.models;

import java.util.ArrayList;

import edu.uwi.sta.srts.controllers.Observer;

public abstract class Subject {

    private ArrayList<Observer> observers;

    protected Subject(){
        this.observers = new ArrayList<>();
    }

    public void attachObserver(Observer observer){
        this.observers.add(observer);
    }

    public void detachObserver(Observer observer){
        this.observers.remove(observer);
    }

    public void notifyObservers(){
        for(Observer observer: observers){
            if(observer != null) {
                observer.update();
            }
        }
    }
}
