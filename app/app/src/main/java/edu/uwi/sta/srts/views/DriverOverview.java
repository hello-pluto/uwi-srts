package edu.uwi.sta.srts.views;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RouteStopsController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.controllers.ShuttlesController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.utils.Utils;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

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

        new ShuttlesController(new Shuttles(), this);
        new RoutesController(new Routes(), this);

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.speedDial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_choose_route:
                        selectRoute();
                        break;
                    case R.id.nav_choose_shuttle:
                        selectShuttle();
                        break;
                }
                return false;
            }
        });
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
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f));

        this.googleMap.setMapStyle(new MapStyleOptions(Utils.getMapStyle()));
        this.googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

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

                    waypoints.sort(new Comparator<LatLng>() {
                        @Override
                        public int compare(LatLng o1, LatLng o2) {
                            return Utils.getEta(location.getLatitude(), location.getLongitude(), o1.latitude, o1.latitude) -
                                    Utils.getEta(location.getLatitude(), location.getLongitude(), o2.latitude, o2.latitude);
                        }
                    });

                    GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                            .from(start)
                            .and(waypoints)
                            .to(start)
                            .transportMode(TransportMode.DRIVING)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    if(direction.isOK()) {
                                        List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
                                        ArrayList<PolylineOptions> polylineOptionList =
                                                DirectionConverter.createTransitPolyline(DriverOverview.this,
                                                        stepList, 5, getResources().getColor(R.color.colorPrimary, null), 3, Color.BLUE);
                                        for (PolylineOptions polylineOption : polylineOptionList) {
                                            googleMap.addPolyline(polylineOption);
                                        }
                                        Info info = direction.getRouteList().get(0).getLegList().get(0).getDuration();
                                        Toast.makeText(DriverOverview.this, info.getText(), Toast.LENGTH_LONG).show();
                                    } else {
                                        // Do something
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    // Do something
                                }
                            });

                    redraw = false;
                }

                if(oldLoc == null || location.distanceTo(oldLoc) > 5) {
                    oldLoc = location;

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

                    DriverOverview.this.googleMap.animateCamera(center);

                    if (shuttleController != null && shuttleController.isShuttleOnDuty()) {
                        shuttleController.setShuttleLocation(new edu.uwi.sta.srts.models.utils.Location(location.getLatitude(), location.getLongitude()));
                        shuttleController.setShuttleRotation(oldLoc.bearingTo(location));
                        shuttleController.saveModel();
                    }
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
        if(model instanceof Routes){
            if(showRoutesDialog) {
                showRoutesDialog = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose a Route");

                final CharSequence[] routes = new CharSequence[((Routes) model).getRoutes().size() + 1];
                for (int i = 0; i < ((Routes) model).getRoutes().size(); i++) {
                    Route route = ((Routes) model).getRoutes().get(i);
                    routes[i] = route.getName();
                }
                routes[((Routes) model).getRoutes().size()] = "None";
                builder.setSingleChoiceItems(routes, routeIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which < routes.length - 1) {
                            routeController = new RouteController(((Routes) model).getRoutes().get(which), null);

                            if (shuttleController != null) {
                                shuttleController.setShuttleRouteId(routeController.getRouteId());
                                shuttleController.saveModel();
                            }

                            TextView routeText = findViewById(R.id.route);
                            routeText.setText(routeController.getRouteName());

                            toolbarText.setText(routeController.getRouteName());

                            RouteStops routeStops = new RouteStops();
                            routeStops.filterSelf(routeController.getRouteId());

                            new RouteStopsController(routeStops, DriverOverview.this);
                        }else{
                            TextView routeText = findViewById(R.id.route);
                            routeText.setText("No route selected");
                        }
                    }
                });
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                for (int i = 0; i < ((Routes) model).getRoutes().size(); i++) {
                    Route route = ((Routes) model).getRoutes().get(i);
                    if(shuttleController != null && shuttleController.getShuttleRouteId().equals(route.getId())){
                        routeIndex = i;
                        routeController = new RouteController(((Routes) model).getRoutes().get(routeIndex), null);

                        if (shuttleController != null) {
                            shuttleController.setShuttleRouteId(routeController.getRouteId());
                        }

                        TextView routeText = findViewById(R.id.route);
                        routeText.setText(routeController.getRouteName());

                        toolbarText.setText(routeController.getRouteName());

                        RouteStops routeStops = new RouteStops();
                        routeStops.filterSelf(routeController.getRouteId());

                        new RouteStopsController(routeStops, DriverOverview.this);
                    }
                }
            }
        }else if(model instanceof Shuttles){
            if(showShuttlesDialog){
                showShuttlesDialog = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose a Shuttle");

                final CharSequence[] shuttles = new CharSequence[((Shuttles)model).getShuttles().size()+1];
                for(int i = 0; i < ((Shuttles)model).getShuttles().size(); i++){
                    Shuttle shuttle = ((Shuttles)model).getShuttles().get(i);
                    shuttles[i] = shuttle.getLicensePlateNo();
                }
                shuttles[((Shuttles)model).getShuttles().size()] = "None";
                builder.setSingleChoiceItems(shuttles, shuttleIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which < shuttles.length-1) {
                            shuttleIndex = which;
                            boolean wasOnDuty = false;
                            if(shuttleController!=null && shuttleController.isShuttleOnDuty()){
                                shuttleController.setShuttleOnDuty(false);
                                shuttleController.saveModel();
                                wasOnDuty = true;
                            }

                            shuttleController = new ShuttleController(((Shuttles) model).getShuttles().get(which), null);
                            shuttleController.setShuttleDriverId(driverController.getUserId());
                            if(wasOnDuty) {
                                shuttleController.setShuttleOnDuty(true);
                            }
                            shuttleController.saveModel();

                            if (routeController != null) {
                                shuttleController.setShuttleRouteId(routeController.getRouteId());
                                shuttleController.saveModel();
                            }

                            TextView shuttleText = findViewById(R.id.plateNo);
                            shuttleText.setText(shuttleController.getShuttleLicensePlateNo());
                        }else{
                            if(shuttleIndex != -1){
                                ShuttleController vc = new ShuttleController(((Shuttles) model).getShuttles().get(shuttleIndex), null);
                                shuttleIndex = -1;
                                vc.setShuttleDriverId("");
                                vc.saveModel();
                            }
                            TextView shuttleText = findViewById(R.id.plateNo);
                            shuttleText.setText("No shuttle selected");
                        }
                    }
                });
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                for(int i = 0; i < ((Shuttles)model).getShuttles().size(); i++){
                    Shuttle shuttle = ((Shuttles)model).getShuttles().get(i);
                    if(shuttle.getDriverId().equals(driverController.getUserId())){
                        shuttleIndex = i;
                        shuttleController = new ShuttleController(((Shuttles) model).getShuttles().get(i), null);
                        shuttleController.setShuttleDriverId(driverController.getUserId());

                        if (routeController != null) {
                            shuttleController.setShuttleRouteId(routeController.getRouteId());
                        }

                        TextView shuttleText = findViewById(R.id.plateNo);
                        shuttleText.setText(shuttleController.getShuttleLicensePlateNo());
                    }
                }
            }

        }else if(model instanceof RouteStops){
            for(RouteStop routeStop : ((RouteStops)model).getRouteStops()){
                new StopController(new Stop(routeStop.getStopId()), this);
            }
            TextView numStops = (TextView) findViewById(R.id.numStops);
            numStops.setText(((RouteStops)model).getRouteStops().size() + " stops");
        }else if(model instanceof Stop){
            stopsHashMap.put(((Stop) model).getId(), (Stop) model);
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
            default:
                toggle.onOptionsItemSelected(item);
                break;
            case R.id.nav_on_duty:
                if(shuttleController!= null){
                    if(shuttleController.isShuttleOnDuty()){
                        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_timer_24);
                        item.setIcon(drawable);
                        item.setTitle("Go on duty");
                        shuttleController.setShuttleOnDuty(false);
                        Toast.makeText(this, "You are now off duty.", Toast.LENGTH_LONG).show();
                    }else{
                        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_timer_off_24);
                        item.setIcon(drawable);
                        item.setTitle("Go off duty");
                        shuttleController.setShuttleOnDuty(true);
                        Toast.makeText(this, "You are now on duty.", Toast.LENGTH_LONG).show();
                    }
                    shuttleController.saveModel();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectRoute(){
        if(shuttleIndex == -1){
            new AlertDialog.Builder(this)
                    .setTitle("Cannot change route")
                    .setMessage("You cannot change the route unless you have selected a shuttle.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            showRoutesDialog = true;
            redraw = true;
            stopsHashMap.clear();
            new RoutesController(new Routes(), this);
        }
    }

    private void selectShuttle(){
        showShuttlesDialog = true;
        new ShuttlesController(new Shuttles(), this);
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
}
