package edu.uwi.sta.srts.views.admin;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.controllers.VehicleController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Vehicle;
import edu.uwi.sta.srts.views.View;

public class EditStop extends AppCompatActivity implements View, OnMapReadyCallback {

    private boolean isEditMode = true;

    private StopController stopController;

    private TextInputLayout nameTextInputLayout;
    private GoogleMap googleMap;

    private boolean isReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stop);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Stop stop = (Stop) getIntent().getSerializableExtra("stop");

        if(stop == null){
            stop = new Stop();
            isEditMode = false;
        }

        stopController = new StopController(stop, this);

        Button done = (Button) findViewById(R.id.done);
        nameTextInputLayout = (TextInputLayout)findViewById(R.id.nameLayout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(isEditMode){
            update(stop);
            getSupportActionBar().setTitle("Edit Stop");
        }else{
            getSupportActionBar().setTitle("Add Stop");
        }

        nameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    nameTextInputLayout.setError(null);
                    stopController.setStopName(s.toString());
                }else{
                    nameTextInputLayout.setError("Must enter a location name");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(nameTextInputLayout.getError() == null){
                    stopController.saveModel();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Model model) {
        if(model instanceof Stop){
            nameTextInputLayout.getEditText().setText(((Stop)model).getName());

            if(isReady) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(((Stop) model).getLocation().getLatitude(), ((Stop) model).getLocation().getLongitude()));
                googleMap.clear();

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(((Stop) model).getLocation().getLatitude(),
                        ((Stop) model).getLocation().getLongitude()), 17f));
                googleMap.addMarker(markerOptions);
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.isReady = true;

        this.googleMap = googleMap;

        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setMapToolbarEnabled(true);

        this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);

                stopController.getStopLocation().setLatitude(latLng.latitude);
                stopController.getStopLocation().setLongitude(latLng.longitude);
            }
        });

        if(isEditMode) {
            update(stopController.getModel());
        }else{
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.641665, -61.3995462), 15f));
        }
    }
}
