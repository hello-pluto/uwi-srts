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
 * This class represents a student user in the system
 */

package edu.uwi.sta.srts.models;

public class Student extends User {

    private String firstName;

    private String lastName;

    /**
     * Default constructor for Firebase
     */
    public Student() {

    }

    /**
     * Constructor that requires a student's email, first name and last name
     * @param email The student user's email
     * @param firstName The student user's first name
     * @param lastName The student user's last name
     */
    public Student(String email, String firstName, String lastName) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
