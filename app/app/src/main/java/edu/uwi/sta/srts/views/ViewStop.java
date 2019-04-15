package edu.uwi.sta.srts.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
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

public class ViewStop extends AppCompatActivity implements View, OnMapReadyCallback {

    private GoogleMap googleMap;

    private StopController stopController;

    private boolean mapReady;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stop);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Stop stop = (Stop) getIntent().getSerializableExtra("stop");

        stopController = new StopController(new Stop(stop.getId()), this);
    }

    @Override
    public void update(Model model) {
        if(model instanceof Stop){
            loaded = true;
            TextView name = (TextView)findViewById(R.id.name);
            name.setText(stopController.getStopName());

            if(mapReady){
                this.googleMap.clear();

                LatLng latLng = new LatLng(stopController.getStopLocation().getLatitude(),
                        stopController.getStopLocation().getLongitude());

                googleMap.addMarker(new MarkerOptions()
                        .icon(Utils.bitmapDescriptorFromVector(this, R.drawable.round_place_24))
                        .position(latLng));

                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, 17f);

                this.googleMap.animateCamera(center);
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        mapReady = true;
        this.googleMap.setPadding((int) Utils.convertDpToPixel(20, this),
                0,
                (int)Utils.convertDpToPixel(20, this),
                (int)Utils.convertDpToPixel(20, this));

        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
        this.googleMap.setMaxZoomPreference(18f);
        this.googleMap.getUiSettings().setAllGesturesEnabled(false);

        this.googleMap.setMapStyle(new MapStyleOptions(Utils.getMapStyle()));

        if(loaded){
            stopController.updateView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loaded) {
            stopController.updateView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.model_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_edit:
                if(loaded) {
                    Intent intent = new Intent(this, EditStop.class);
                    intent.putExtra("stop", stopController.getModel());
                    startActivity(intent);
                }
                return true;
            case R.id.nav_delete:
                stopController.getModel().delete();
                finish();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
