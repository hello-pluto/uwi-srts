package edu.uwi.sta.srts.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.utils.Utils;

public class EditStop extends AppCompatActivity implements View, OnMapReadyCallback {

    private boolean isEditMode = true;

    private StopController stopController;

    private EditText nameEditText;
    private GoogleMap googleMap;

    private boolean isReady = false;
    private boolean hasMarker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stop);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarText = (TextView)findViewById(R.id.toolbarText);

        Stop stop = (Stop) getIntent().getSerializableExtra("stop");

        if(stop == null){
            stop = new Stop();
            isEditMode = false;
        }

        stopController = new StopController(stop, this);

        android.view.View done = findViewById(R.id.done);
        nameEditText = (EditText) findViewById(R.id.name);

        Utils.setUpActivations(this, nameEditText, findViewById(R.id.nameUnderline));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(isEditMode){
            update(stop);
            toolbarText.setText("Edit Stop");
        }else{
            toolbarText.setText("Add Stop");
        }

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    findViewById(R.id.nameDone).setVisibility(android.view.View.VISIBLE);
                    stopController.setStopName(s.toString());
                }else{
                    ((TextView)findViewById(R.id.nameError)).setText("Must enter a location name");
                    findViewById(R.id.nameDone).setVisibility(android.view.View.GONE);
                }

                if(((TextView)findViewById(R.id.nameError)).getVisibility() == android.view.View.VISIBLE){
                    ((TextView)findViewById(R.id.nameError)).setVisibility(android.view.View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                boolean error = false;

                if(findViewById(R.id.nameDone).getVisibility() != android.view.View.VISIBLE){
                    findViewById(R.id.nameError).setVisibility(android.view.View.VISIBLE);
                    findViewById(R.id.nameUnderline).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    error = true;
                }

                if(!hasMarker){
                    Toast.makeText(EditStop.this, "Add a location on the map first", Toast.LENGTH_LONG).show();
                    error = true;
                }

                if(!error){
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
            nameEditText.setText(((Stop)model).getName());
            findViewById(R.id.nameDone).setVisibility(android.view.View.VISIBLE);

            if(isReady) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(((Stop) model).getLocation().getLatitude(), ((Stop) model).getLocation().getLongitude()));
                googleMap.clear();

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(((Stop) model).getLocation().getLatitude(),
                        ((Stop) model).getLocation().getLongitude()), 17f));
                googleMap.addMarker(markerOptions);
                hasMarker = true;
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
        this.googleMap.setMapStyle(new MapStyleOptions(Utils.getMapStyle()));

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

                hasMarker = true;
            }
        });

        if(isEditMode) {
            update(stopController.getModel());
        }else{
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.641665, -61.3995462), 15f));
        }
    }
}
