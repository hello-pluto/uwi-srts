package edu.uwi.sta.srts.views;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RouteStopsController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.controllers.ShuttlesController;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.utils.Utils;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class DriverOverview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View {

    private GoogleMap googleMap;
    private ActionBarDrawerToggle toggle;

    private UserController driverController;
    private ShuttleController shuttleController;
    private RouteController routeController;
    private HashMap<String, Stop> stopsHashMap = new HashMap<>();

    private boolean showRoutesDialog = false;
    private boolean showShuttlesDialog = false;

    private Location oldLoc = null;

    private boolean redraw = true;

    private int shuttleIndex = -1;
    private int routeIndex = -1;

    private TextView toolbarText;

    private FloatingActionButton fab;
    private ObjectAnimator scaleDown;
    private PulsatorLayout pulsator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_overview);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarText = (TextView) findViewById(R.id.toolbarText);

        final Snackbar offlineSnackbar = Snackbar.make(findViewById(R.id.content_frame),
                "No internet. All changes saved locally.", Snackbar.LENGTH_INDEFINITE);
        offlineSnackbar.setAction("Dismiss", new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                offlineSnackbar.dismiss();
            }
        });

        DatabaseHelper.attachIsOnlineListener(offlineSnackbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.view.View headerLayout = navigationView.getHeaderView(0);

        TextView navName = (TextView) headerLayout.findViewById(R.id.navName);
        TextView navEmail = (TextView) headerLayout.findViewById(R.id.navEmail);
        TextView navType = (TextView) headerLayout.findViewById(R.id.navType);
        FloatingActionButton navEditUser = (FloatingActionButton)headerLayout.findViewById(R.id.edit_user);
        navEditUser.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(DriverOverview.this, EditUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        driverController = new UserController((User)getIntent().getSerializableExtra("user"),
                new DriverOverview.UserView(navName, navType, navEmail));

        driverController.getModel().notifyObservers();

        fab = (FloatingActionButton) findViewById(R.id.onDuty);

        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                fab,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown.setDuration(500)
                .setStartDelay(200);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());

        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);

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

        TextView routeText = findViewById(R.id.route);
        routeText.setText(routeController.getRouteName());

        toolbarText.setText(routeController.getRouteName());

        TextView shuttleText = findViewById(R.id.plateNo);
        shuttleText.setText(shuttleController.getShuttleLicensePlateNo());

        RouteStops routeStops = new RouteStops();
        routeStops.filterSelf(routeController.getRouteId());
        new RouteStopsController(routeStops, this).updateView();


        setOnDuty(getIntent().getBooleanExtra("onDuty", false));
    }

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
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
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
                    List<LatLng> waypoints = new ArrayList<>();
                    waypoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    for (Stop stop : stopsHashMap.values()) {
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(stop.getLocation().getLatitude(), stop.getLocation().getLongitude()))
                                .title(stop.getName()).snippet(
                                        String.valueOf(Utils.getEta(location.getLatitude(), location.getLongitude(),
                                                stop.getLocation().getLatitude(), stop.getLocation().getLongitude())))
                                .icon(Utils.bitmapDescriptorFromVector(DriverOverview.this, R.drawable.round_place_24)));
                        waypoints.add(new LatLng(stop.getLocation().getLatitude(), stop.getLocation().getLongitude()));
                    }

                    LatLng start = waypoints.remove(0);
                    LatLng end = start;
                    if(waypoints.size() != 0) {
                        end = waypoints.remove(waypoints.size() - 1);
                    }

                    waypoints.sort(new Comparator<LatLng>() {
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
                                                    googleMap.addPolyline(polylineOption);
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
                                            googleMap.addPolyline(polylineOption);
                                        }
                                        Info info = direction.getRouteList().get(0).getLegList().get(0).getDuration();
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    // Do something
                                }
                            });

                    redraw = false;
                }

                if(oldLoc == null || location.distanceTo(oldLoc) > 1) {

                    if(oldLoc==null){
                        oldLoc = location;
                    }

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

                    DriverOverview.this.googleMap.animateCamera(center);

                    if (shuttleController != null && shuttleController.isShuttleOnDuty()) {
                        shuttleController.setShuttleLocation(new edu.uwi.sta.srts.models.utils.Location(location.getLatitude(), location.getLongitude()));
                        shuttleController.setShuttleRotation(oldLoc.bearingTo(location));
                        shuttleController.saveModel();
                    }

                    oldLoc = location;
                }
            }
        });
    }

    public class UserView implements edu.uwi.sta.srts.views.View {

        TextView name;
        TextView type;
        TextView email;

        public UserView(TextView name, TextView type, TextView email){
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
    public boolean onNavigationItemSelected(MenuItem item) {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
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

                TextView routeText = findViewById(R.id.route);
                routeText.setText(routeController.getRouteName());

                toolbarText.setText(routeController.getRouteName());

                TextView shuttleText = findViewById(R.id.plateNo);
                shuttleText.setText(shuttleController.getShuttleLicensePlateNo());

                RouteStops routeStops = new RouteStops();
                routeStops.filterSelf(routeController.getRouteId());
                new RouteStopsController(routeStops, this).updateView();

                setOnDuty(data.getBooleanExtra("onDuty", false));
            }
        }
    }
}
