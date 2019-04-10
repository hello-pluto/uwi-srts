package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

public class Alerts extends Model {

    private ArrayList<Alert> alerts = new ArrayList<>();

    /**
     * Default constructor that fetches all alerts from the database
     */
    public Alerts() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("alerts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        alerts.clear();
                        for (DataSnapshot route: dataSnapshot.getChildren()) {
                            Alert a = route.getValue(Alert.class);
                            alerts.add(a);
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public ArrayList<Alert> getAlerts() {
        return alerts;
    }

    public void addAlert(Alert alert){
        this.alerts.add(alert);
    }

    public void removeAlert(int index){
        this.alerts.remove(index);
    }

    @Override
    public void save() {
        for(Alert alert: this.getAlerts()){
            alert.save();
        }
    }

    @Override
    public void delete() {
        for(Alert alert: this.getAlerts()){
            alert.delete();
        }
    }
}
