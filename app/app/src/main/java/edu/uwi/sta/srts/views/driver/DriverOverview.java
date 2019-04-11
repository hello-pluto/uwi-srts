package edu.uwi.sta.srts.views.driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RouteStopsController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.controllers.StopsController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.UsersController;
import edu.uwi.sta.srts.controllers.VehicleController;
import edu.uwi.sta.srts.controllers.VehiclesController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Vehicle;
import edu.uwi.sta.srts.models.Vehicles;
import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.utils.Utils;
import edu.uwi.sta.srts.views.LoginActivity;
import edu.uwi.sta.srts.views.View;
import edu.uwi.sta.srts.views.admin.EditUser;
import edu.uwi.sta.srts.views.fragments.AlertsFragment;
import edu.uwi.sta.srts.views.fragments.RoutesFragment;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class DriverOverview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View {

    private GoogleMap googleMap;
    private ActionBarDrawerToggle toggle;

    private UserController driverController;
    private VehicleController vehicleController;
    private RouteController routeController;
    private HashMap<String, Stop> stopsHashMap = new HashMap<>();

    private boolean showRoutesDialog = false;
    private boolean showVehiclesDialog = false;

    private Location oldLoc = null;

    private boolean redraw = true;

    private int vehicleIndex = -1;
    private int routeIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_overview);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

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
                new DriverOverview.UserView(navName, navEmail, (TextView) findViewById(R.id.driver)));

        driverController.getModel().notifyObservers();

        new VehiclesController(new Vehicles(), this);
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
            return;
        }



        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.setTrafficEnabled(true);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f));

        this.googleMap.setMapStyle(new MapStyleOptions(Utils.getMapStyle()));
        this.googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        this.googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
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

                if(oldLoc == null || location.distanceTo(oldLoc) > 5) {
                    oldLoc = location;

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

                    DriverOverview.this.googleMap.animateCamera(center);

                    if (vehicleController != null) {
                        vehicleController.setVehicleLocation(new edu.uwi.sta.srts.models.utils.Location(location.getLatitude(), location.getLongitude()));
                        vehicleController.saveModel();
                    }


                }
            }
        });
    }

    public class UserView implements edu.uwi.sta.srts.views.View {

        TextView name;
        TextView name2;
        TextView email;

        public UserView(TextView name, TextView email, TextView name2){
            this.name = name;
            this.email = email;
            this.name2 = name2;
        }

        @Override
        public void update(Model model) {
            if(model instanceof User && name != null && name2 != null && email != null){
                this.name.setText(((User)model).getFullName());
                this.name2.setText(((User)model).getFullName());
                this.email.setText(((User)model).getEmail());
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

                            if (vehicleController != null) {
                                vehicleController.setVehicleRouteId(routeController.getRouteId());
                                vehicleController.saveModel();
                            }

                            TextView routeText = findViewById(R.id.route);
                            routeText.setText(routeController.getRouteName());

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
                    if(vehicleController != null && vehicleController.getVehicleRouteId().equals(route.getId())){
                        routeIndex = i;
                        routeController = new RouteController(((Routes) model).getRoutes().get(routeIndex), null);

                        if (vehicleController != null) {
                            vehicleController.setVehicleRouteId(routeController.getRouteId());
                        }

                        TextView routeText = findViewById(R.id.route);
                        routeText.setText(routeController.getRouteName());

                        RouteStops routeStops = new RouteStops();
                        routeStops.filterSelf(routeController.getRouteId());

                        new RouteStopsController(routeStops, DriverOverview.this);
                    }
                }
            }
        }else if(model instanceof Vehicles){
            if(showVehiclesDialog){
                showVehiclesDialog = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose a Shuttle");

                final CharSequence[] vehicles = new CharSequence[((Vehicles)model).getVehicles().size()+1];
                for(int i = 0; i < ((Vehicles)model).getVehicles().size(); i++){
                    Vehicle vehicle = ((Vehicles)model).getVehicles().get(i);
                    vehicles[i] = vehicle.getLicensePlateNo();
                }
                vehicles[((Vehicles)model).getVehicles().size()] = "None";
                builder.setSingleChoiceItems(vehicles, vehicleIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which < vehicles.length-1) {
                            vehicleIndex = which;
                            vehicleController = new VehicleController(((Vehicles) model).getVehicles().get(which), null);
                            vehicleController.setVehicleDriverId(driverController.getUserId());
                            vehicleController.saveModel();

                            if (routeController != null) {
                                vehicleController.setVehicleRouteId(routeController.getRouteId());
                                vehicleController.saveModel();
                            }

                            TextView vehicleText = findViewById(R.id.plateNo);
                            vehicleText.setText(vehicleController.getVehicleLicensePlateNo());
                        }else{
                            if(vehicleIndex != -1){
                                VehicleController vc = new VehicleController(((Vehicles) model).getVehicles().get(vehicleIndex), null);
                                vehicleIndex = -1;
                                vc.setVehicleDriverId("");
                                vc.saveModel();
                            }
                            TextView vehicleText = findViewById(R.id.plateNo);
                            vehicleText.setText("No shuttle selected");
                        }
                    }
                });
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                for(int i = 0; i < ((Vehicles)model).getVehicles().size(); i++){
                    Vehicle vehicle = ((Vehicles)model).getVehicles().get(i);
                    if(vehicle.getDriverId().equals(driverController.getUserId())){
                        vehicleIndex = i;
                        vehicleController = new VehicleController(((Vehicles) model).getVehicles().get(i), null);
                        vehicleController.setVehicleDriverId(driverController.getUserId());

                        if (routeController != null) {
                            vehicleController.setVehicleRouteId(routeController.getRouteId());
                        }

                        TextView vehicleText = findViewById(R.id.plateNo);
                        vehicleText.setText(vehicleController.getVehicleLicensePlateNo());
                    }
                }
            }

        }else if(model instanceof RouteStops){
            for(RouteStop routeStop : ((RouteStops)model).getRouteStops()){
                new StopController(new Stop(routeStop.getStopId()), this);
            }
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
        getMenuInflater().inflate(R.menu.admin_overview, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectRoute(){
        if(vehicleIndex == -1){
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
        showVehiclesDialog = true;
        new VehiclesController(new Vehicles(), this);
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
}
