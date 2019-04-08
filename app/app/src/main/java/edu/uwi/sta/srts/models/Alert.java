package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

public class Alert extends Model {

    private String alertId;

    private String message;

    /**
     * Default constructor for Firebase
     */
    public Alert () {
        super();
    }

    /**
     * Constructor that fetches the route corresponding to the given routeId
     * @param alertId The alertId of the alert to fetch
     */
    public Alert(String alertId){
        super();
        DatabaseHelper.getInstance().getDatabaseReference("alerts").child(alertId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Alert a = dataSnapshot.getValue(Alert.class);
                        if(a != null) {
                            Alert.this.setMessage(a.getMessage());
                            Alert.this.setId(a.getId());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    /**
     * Constructor that create a new route object and requires a route name and frequency
     * @param alertId The id of the alert
     * @param message The message alert displayed to the user
     */
    public Alert(String alertId, String message) {
        this.alertId = alertId;
        this.message = message;
    }


    public String getAlertId() {
        return alertId;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void save() {

    }
}
