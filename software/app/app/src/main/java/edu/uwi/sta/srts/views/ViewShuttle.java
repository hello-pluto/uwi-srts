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

package edu.uwi.sta.srts.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.UserType;
import edu.uwi.sta.srts.utils.Utils;

public class ViewShuttle extends AppCompatActivity implements View, OnMapReadyCallback{

    private ShuttleController shuttleController;
    private GoogleMap googleMap;

    private boolean mapReady = false;
    private Location location;

    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shuttle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Shuttle shuttle = (Shuttle) getIntent().getSerializableExtra("shuttle");

        shuttleController = new ShuttleController(new Shuttle(shuttle.getId()), this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                5, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        ViewShuttle.this.location = location;
                        if(loaded){
                            shuttleController.update();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });

        if(getIntent().getBooleanExtra("isAdmin", false)){
            findViewById(R.id.etaLayout).setVisibility(android.view.View.GONE);
        }else {
            TextView eta = findViewById(R.id.eta);
            eta.setText(getIntent().getStringExtra("eta"));
        }
    }

    @Override
    public void update(Model model) {
        if(model instanceof Shuttle){
            loaded = true;

            new UserController(new User(shuttleController.getShuttleDriverId()), new DriverView((TextView) findViewById(R.id.driver)));
            new RouteController(new Route(shuttleController.getShuttleRouteId()), new RouteView((TextView) findViewById(R.id.route)));

            TextView plateNo = findViewById(R.id.plateNo);
            plateNo.setText(shuttleController.getShuttleLicensePlateNo());

            TextView capacity = findViewById(R.id.capacity);
            capacity.setText(shuttleController.getShuttleCapacity() + " seats");

            if(location != null) {
                TextView eta =  findViewById(R.id.eta);
                int etaInMinutes = Utils.getEta(location.getLatitude(), location.getLongitude(),
                        shuttleController.getShuttleLocation().getLatitude(),
                        shuttleController.getShuttleLocation().getLongitude());

                eta.setText(Utils.formatEta(etaInMinutes));
            }

            if(mapReady){
                this.googleMap.clear();

                LatLng latLng = new LatLng(shuttleController.getShuttleLocation().getLatitude(),
                        shuttleController.getShuttleLocation().getLongitude());

                googleMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(Utils.bitmapDescriptorFromVector(this, R.drawable.shuttle))
                        .bearing(shuttleController.getShuttleRotation())
                        .position(latLng,20)
                        .clickable(true));

                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, 18f);

                this.googleMap.animateCamera(center);
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        mapReady = true;
        this.googleMap.setPadding((int)Utils.convertDpToPixel(20, this),
                0,
                (int)Utils.convertDpToPixel(20, this),
                (int)Utils.convertDpToPixel(20, this));

        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
        this.googleMap.setMaxZoomPreference(18f);
        this.googleMap.getUiSettings().setAllGesturesEnabled(false);

        this.googleMap.setMapStyle(new MapStyleOptions(Utils.getMapStyle()));

        if(loaded) {
            shuttleController.update();
        }
    }

    public class DriverView implements edu.uwi.sta.srts.views.View {

        TextView textView;

        private DriverView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof User && textView != null){
                this.textView.setText(((User)model).getFullName());
            }
        }
    }

    public class RouteView implements edu.uwi.sta.srts.views.View {

        TextView textView;

        private RouteView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof Route && textView != null){
                this.textView.setText(((Route)model).getName());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loaded) {
            shuttleController.update();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getIntent().getBooleanExtra("isAdmin", false)){
            getMenuInflater().inflate(R.menu.model_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_edit:
                if(shuttleController.getModel() != null) {
                    Intent intent = new Intent(this, EditShuttle.class);
                    intent.putExtra("shuttle", shuttleController.getModel());
                    startActivity(intent);
                }
                return true;
            case R.id.nav_delete:
                shuttleController.getModel().delete();
                finish();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
