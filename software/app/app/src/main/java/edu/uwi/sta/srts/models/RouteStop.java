package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;

public class RouteStop extends Model {

    private int order;

    private String routeId;

    private String stopId;

    /**
     * Default constructor for Firebase
     */
    public RouteStop(){
        super();
    }

    /**
     * Constructor that fetches the route-stop corresponding to the given stopId
     * @param routeStopId The id of the route-stop to fetch
     */
    public RouteStop(String routeStopId){
        super();
        DatabaseHelper.getInstance().getDatabaseReference("routeStop").child(routeStopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RouteStop s = dataSnapshot.getValue(RouteStop.class);
                        if(s != null) {
                            RouteStop.this.setRouteId(s.getRouteId());
                            RouteStop.this.setStopId(s.getStopId());
                            RouteStop.this.setOrder(s.getOrder());

                            notifyObservers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public RouteStop(String routeId, String stopId) {
        this.routeId = routeId;
        this.stopId = stopId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteStopId() {
        return id;
    }

    public void setRouteStopId(String routeStopId) {
        this.id = routeStopId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    @Override
    public void save() {
        if(getId().equals("")){
            DatabaseReference ref = DatabaseHelper.getInstance().getDatabaseReference("routeStops").push();
            this.setId(ref.getKey());
            ref.setValue(this);
        }else{
            DatabaseHelper.getInstance().getDatabaseReference("routeStops")
                    .child(getId()).setValue(this);
        }
    }

    @Override
    public void delete() {
        if(!getId().equals("")){
            DatabaseHelper.getInstance().getDatabaseReference("routeStops")
                    .child(getId()).setValue(null);
        }
    }
}
