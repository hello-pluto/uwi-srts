package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

public class Alert extends Model {

    private String title;

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
                            Alert.this.setTitle(a.getTitle());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("alerts").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("alerts")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")) {
            DatabaseHelper.getInstance().getDatabaseReference("alerts")
                    .child(getId()).setValue(null);
        }
    }
}
