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
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RouteStopsController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.SimpleLocation;
import edu.uwi.sta.srts.utils.Utils;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class DriverOverview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View {

    private GoogleMap googleMap;
    private ActionBarDrawerToggle toggle;

    private UserController driverController;
    private ShuttleController shuttleController;
    private RouteController routeController;
    private HashMap<String, Stop> stopsHashMap = new HashMap<>();

    private Location oldLoc = null;

    private boolean redraw = true;

    private TextView toolbarText;

    private FloatingActionButton fab;
    private ObjectAnimator scaleDown;
    private PulsatorLayout pulsator;

    private List<Polyline> polylines = new ArrayList<Polyline>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_overview);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarText = (TextView) findViewById(R.id.toolbarText);

        Utils.setupOfflineSnackbarListener(findViewById(R.id.content_frame));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.view.View headerLayout = navigationView.getHeaderView(0);

        TextView navName = headerLayout.findViewById(R.id.navName);
        TextView navEmail = headerLayout.findViewById(R.id.navEmail);
        TextView navType = headerLayout.findViewById(R.id.navType);
        FloatingActionButton navEditUser = (FloatingActionButton)headerLayout.findViewById(R.id.edit_user);
        navEditUser.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(DriverOverview.this, EditUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        headerLayout.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(DriverOverview.this, ViewUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                intent.putExtra("isAdmin", false);
                startActivity(intent);
            }
        });

        driverController = new UserController((User)getIntent().getSerializableExtra("user"),
                new DriverOverview.UserView(navName, navType, navEmail));

        driverController.getModel().notifyObservers();

        fab = findViewById(R.id.onDuty);

        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                fab,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown.setDuration(500)
                .setStartDelay(200);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());

        pulsator = findViewById(R.id.pulsator);

        fab.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (shuttleController != null){
                    if(shuttleController.isShuttleOnDuty()) {
                        setOnDuty(false);
                    }else{
                        setOnDuty(true);
                    }
                }
            }
        });

        shuttleController = new ShuttleController((Shuttle)getIntent().getSerializableExtra("shuttle"), this);
        routeController = new RouteController((Route)getIntent().getSerializableExtra("route"), this);

        shuttleController.setShuttleRouteId(routeController.getRouteId());
        shuttleController.saveModel();

        TextView routeText = findViewById(R.id.route);
        routeText.setText(routeController.getRouteName());

        toolbarText.setText(routeController.getRouteName());

        TextView shuttleText = findViewById(R.id.plateNo);
        shuttleText.setText(shuttleController.getShuttleLicensePlateNo());

        RouteStops routeStops = new RouteStops();
        routeStops.filter(routeController.getRouteId());
        new RouteStopsController(routeStops, this).update();

        setOnDuty(getIntent().getBooleanExtra("onDuty", false));
    }

    /**
     * Sets the on duty status of a driver and updates the UI accordingly
     * @param onDuty Whether or not the driver is on duty
     */
    public void setOnDuty(boolean onDuty){
        if(onDuty) {
            if(shuttleController != null) {
                shuttleController.setShuttleOnDuty(true);
                shuttleController.setShuttleDriverId(driverController.getUserId());
                shuttleController.saveModel();
            }
            pulsator.start();
            scaleDown.start();
            scaleDown.setRepeatCount(ValueAnimator.INFINITE);
            pulsator.setVisibility(android.view.View.VISIBLE);
            fab.setImageDrawable(getDrawable(R.drawable.round_timer_24));
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
            Snackbar.make(findViewById(R.id.content_frame), "You are now on duty", Snackbar.LENGTH_SHORT).show();
        }else{
            if(shuttleController != null) {
                shuttleController.setShuttleOnDuty(false);
                shuttleController.setShuttleDriverId("");
                shuttleController.saveModel();
            }
            pulsator.setVisibility(android.view.View.INVISIBLE);
            scaleDown.setRepeatCount(1);
            fab.setImageDrawable(getDrawable(R.drawable.round_timer_off_24));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f44336")));
            Snackbar.make(findViewById(R.id.content_frame), "You are now off duty", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

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

        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.setTrafficEnabled(false);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.642830, -61.399385), 15f));

        this.googleMap.setMapStyle(new MapStyleOptions(Utils.getMapStyle()));
        this.googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Stop stop = stopsHashMap.get(marker.getTitle());

                Intent intent = new Intent(DriverOverview.this, ViewStop.class);
                intent.putExtra("stop", stop);
                startActivity(intent);

            }
        });

        this.googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(final Location location) {
                if(redraw) {
                    DriverOverview.this.googleMap.clear();

                    for (Stop stop : stopsHashMap.values()) {
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(stop.getLocation().getLatitude(), stop.getLocation().getLongitude()))
                                .title(stop.getName()).snippet(
                                        String.valueOf(Utils.getEta(location.getLatitude(), location.getLongitude(),
                                                stop.getLocation().getLatitude(), stop.getLocation().getLongitude())))
                                .icon(Utils.bitmapDescriptorFromVector(DriverOverview.this, R.drawable.round_place_24)));
                    }

                    redraw = false;
                }

                if(oldLoc == null || location.distanceTo(oldLoc) > 1) {

                    if(oldLoc==null){
                        oldLoc = location;
                    }

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

                    DriverOverview.this.googleMap.animateCamera(center);

                    if (shuttleController != null && shuttleController.isShuttleOnDuty()) {
                        shuttleController.setShuttleLocation(new SimpleLocation(location.getLatitude(), location.getLongitude()));
                        shuttleController.setShuttleRotation(oldLoc.bearingTo(location));
                        shuttleController.saveModel();
                    }

                    List<LatLng> waypoints = new ArrayList<>();
                    waypoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    for (Stop stop : stopsHashMap.values()) {
                        waypoints.add(new LatLng(stop.getLocation().getLatitude(), stop.getLocation().getLongitude()));
                    }
                    LatLng start = waypoints.remove(0);
                    LatLng end = start;
                    if(waypoints.size() != 0) {
                        end = waypoints.remove(waypoints.size() - 1);
                    }

                    Collections.sort(waypoints, new Comparator<LatLng>() {
                        @Override
                        public int compare(LatLng o1, LatLng o2) {
                            Location l1 = new Location("");
                            l1.setLatitude(o1.latitude);
                            l1.setLongitude(o1.longitude);

                            Location l2 = new Location("");
                            l2.setLatitude(o2.latitude);
                            l2.setLongitude(o2.longitude);
                            return (int)(location.distanceTo(l1) - location.distanceTo(l2));
                        }
                    });



                    GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                            .from(start)
                            .and(waypoints)
                            .to(end)
                            .transportMode(TransportMode.DRIVING)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    if(direction.isOK()) {
                                        for(Polyline polyline: polylines){
                                            polyline.remove();
                                        }

                                        polylines.clear();

                                        int i = 0;
                                        for(Leg leg : direction.getRouteList().get(0).getLegList()) {
                                            List<Step> stepList = leg.getStepList();
                                            if(i != 0) {
                                                ArrayList<PolylineOptions> polylineOptionList =
                                                        DirectionConverter.createTransitPolyline(DriverOverview.this,
                                                                stepList, 5, Color.parseColor("#B2DFDB"), 3, Color.BLUE);
                                                for (PolylineOptions polylineOption : polylineOptionList) {
                                                    polylineOption.startCap(new RoundCap());
                                                    polylineOption.endCap(new RoundCap());
                                                    polylines.add(googleMap.addPolyline(polylineOption));
                                                }
                                            }
                                            i++;
                                        }

                                        ArrayList<PolylineOptions> polylineOptionList =
                                                DirectionConverter.createTransitPolyline(DriverOverview.this,
                                                        direction.getRouteList().get(0).getLegList().get(0).getStepList(),
                                                        5, Color.parseColor("#4DB6AC")
                                                        , 3, Color.BLUE);
                                        for (PolylineOptions polylineOption : polylineOptionList) {
                                            polylineOption.startCap(new RoundCap());
                                            polylineOption.endCap(new RoundCap());
                                            polylines.add(googleMap.addPolyline(polylineOption));
                                        }
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {}
                            });

                    oldLoc = location;
                }
            }
        });
    }

    @Override
    public void update(final Model model) {
        if(model instanceof RouteStops){
            stopsHashMap.clear();
            redraw = true;
            for(RouteStop routeStop : ((RouteStops)model).getRouteStops()){
                new StopController(new Stop(routeStop.getStopId()), this);
            }
            TextView numStops = (TextView) findViewById(R.id.numStops);
            numStops.setText(((RouteStops)model).getRouteStops().size() + " stops");

        }else if(model instanceof Stop){
            stopsHashMap.put(((Stop) model).getName(), (Stop) model);
            redraw = true;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_overview, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_routes:
                startActivity(new Intent(this, ViewRoutes.class));
                break;
            case R.id.nav_alerts:
                startActivity(new Intent(this, ViewAlerts.class));
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_change_shuttle_and_route:
                Intent intent = new Intent(this, DriverSetup.class);
                intent.putExtra("user", driverController.getModel());
                startActivityForResult(intent, 1);
                break;
            default:
                toggle.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final android.view.View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.map_info_window, null);
        }

        @Override
        public android.view.View getInfoContents(Marker marker) {

            return null;
        }

        @Override
        public android.view.View getInfoWindow(Marker marker) {
            TextView eta = ((TextView)myContentsView.findViewById(R.id.eta));
            eta.setText(marker.getSnippet());
            TextView title = ((TextView)myContentsView.findViewById(R.id.name));
            title.setText(marker.getTitle());
            return myContentsView;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    try {
                        mapFragment.getMapAsync(this);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.content_frame), "Cannot use app without location permission", Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(shuttleController!=null) {
            shuttleController.setShuttleOnDuty(false);
            shuttleController.setShuttleDriverId("");
            shuttleController.saveModel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                if(shuttleController!=null) {
                    shuttleController.setShuttleOnDuty(false);
                    shuttleController.setShuttleDriverId("");
                    shuttleController.saveModel();
                }

                shuttleController = new ShuttleController((Shuttle)data.getSerializableExtra("shuttle"), this);
                routeController = new RouteController((Route)data.getSerializableExtra("route"), this);

                shuttleController.setShuttleRouteId(routeController.getRouteId());
                shuttleController.saveModel();

                TextView routeText = findViewById(R.id.route);
                routeText.setText(routeController.getRouteName());

                toolbarText.setText(routeController.getRouteName());

                TextView shuttleText = findViewById(R.id.plateNo);
                shuttleText.setText(shuttleController.getShuttleLicensePlateNo());

                RouteStops routeStops = new RouteStops();
                routeStops.filter(routeController.getRouteId());
                new RouteStopsController(routeStops, this).update();

                setOnDuty(data.getBooleanExtra("onDuty", false));
            }
        }
    }

    public class UserView implements edu.uwi.sta.srts.views.View {

        TextView name;
        TextView type;
        TextView email;

        private UserView(TextView name, TextView type, TextView email){
            this.name = name;
            this.type = type;
            this.email = email;
        }

        @Override
        public void update(Model model) {
            if(model instanceof User && name != null && email != null){
                this.name.setText(((User)model).getFullName());
                this.email.setText(((User)model).getEmail());
                this.type.setText("Driver");
            }
        }
    }
}
