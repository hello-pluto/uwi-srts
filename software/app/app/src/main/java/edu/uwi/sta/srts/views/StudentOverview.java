package edu.uwi.sta.srts.views;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.controllers.ShuttlesController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.utils.DatabaseHelper;
import edu.uwi.sta.srts.utils.Utils;

public class StudentOverview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View {

    private GoogleMap googleMap;
    private ActionBarDrawerToggle toggle;

    private RoutesController routesController;
    private ShuttlesController shuttlesController;
    private ShuttleController shuttleController;

    private boolean mapReady = false;
    private boolean dataLoaded = false;

    private HashMap<LatLng, Shuttle> latLngShuttleHashMap;

    private int routeIndex = -1;

    private Location location;

    private TextView toolbarText;

    private android.view.View shuttleDetails;
    private FloatingActionButton filter;

    private boolean shuttleDetailsVisible = false;

    private float previousZoomLevel = -1.0f;
    private ArrayList<GroundOverlay> groundOverlays = new ArrayList<>();

    private CountDownTimer etaTimer;
    private String etaText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_overview);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarText = findViewById(R.id.toolbarText);

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
        FloatingActionButton navEditUser = headerLayout.findViewById(R.id.edit_user);
        navEditUser.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(StudentOverview.this, EditUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        headerLayout.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(StudentOverview.this, ViewUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                intent.putExtra("isAdmin", false);
                startActivity(intent);
            }
        });

        latLngShuttleHashMap = new HashMap<>();

        new UserController((User)getIntent().getSerializableExtra("user"),
                new StudentOverview.UserView(navName, navType, navEmail)).update();

        shuttlesController = new ShuttlesController(new Shuttles(), this);

        routesController = new RoutesController(new Routes(), this);

        filter = findViewById(R.id.filter);
        filter.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                android.view.View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.filter_header, null);
                TextView titleText = view.findViewById(R.id.title);
                final TextView messageText = view.findViewById(R.id.message);
                titleText.setText(getString(R.string.filter_shuttle_by_route));
                messageText.setText(getString(R.string.select_a_route_to_show_shuttles_for));

                final CharSequence[] routes = new CharSequence[routesController.getRoutes().size() + 1];
                for (int i = 0; i < routesController.getRoutes().size(); i++) {
                    Route route = routesController.getRoutes().get(i);
                    routes[i] = route.getName();
                }
                routes[routesController.getRoutes().size()] = "None";

                int index = routeIndex;
                if(routeIndex == -1){
                    index = routes.length -1;
                }

                new AlertDialog.Builder(StudentOverview.this)
                        .setSingleChoiceItems(routes, index, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        if(which < routes.length - 1) {
                                            routeIndex = which;
                                        }else{
                                            routeIndex = -1;
                                        }
                                    }
                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(routeIndex != -1) {
                                    Route route = routesController.getRoutes().get(routeIndex);
                                    toolbarText.setText(route.getName());
                                }else{
                                    toolbarText.setText(getString(R.string.all_shuttles));
                                }
                                if(mapReady && dataLoaded) {
                                    populateMap();
                                }
                                hideShuttleDetails();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCustomTitle(view).create().show();
            }
        });

        shuttleDetails = findViewById(R.id.shuttleDetails);

        findViewById(R.id.shuttleDetailsClick).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(shuttleDetailsVisible){
                    Shuttle shuttle = (Shuttle) shuttleController.getModel();
                    Intent intent = new Intent(StudentOverview.this, ViewShuttle.class);
                    intent.putExtra("shuttle", shuttle);
                    intent.putExtra("eta", etaText);
                    startActivity(intent);
                }
            }
        });

        hideShuttleDetails();
    }

    public void updateEta(){
        final TextView eta = findViewById(R.id.eta);
        if(location != null && shuttleController!=null) {
            int etaInMinutes = Utils.getEta(location.getLatitude(), location.getLongitude(),
                    shuttleController.getShuttleLocation().getLatitude(), shuttleController.getShuttleLocation().getLongitude());

            if(etaTimer != null){
                etaTimer.cancel();
            }

            etaTimer = new CountDownTimer(etaInMinutes*60*1000, 60*1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    etaText = Utils.formatEta((int)millisUntilFinished/60/1000);
                    eta.setText(etaText);
                }

                @Override
                public void onFinish() {
                    eta.setText("a few seconds away");
                }
            };
            etaTimer.start();
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
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
        this.googleMap.setMaxZoomPreference(18f);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        this.googleMap.setMapStyle(new MapStyleOptions(Utils.getMapStyle()));

        this.googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(StudentOverview.this.location!=null && StudentOverview.this.location.distanceTo(location) > 100){
                    updateEta();
                }
                StudentOverview.this.location = location;
            }
        });

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = googleMap.getCameraPosition();

                if(previousZoomLevel != cameraPosition.zoom) {
                    for(GroundOverlay groundOverlay : groundOverlays){
                        double metersPerPx = 156543.03392 * Math.cos(groundOverlay.getPosition().latitude * Math.PI / 180) / Math.pow(2, cameraPosition.zoom);
                        groundOverlay.setDimensions(20 + (int)metersPerPx * 25);
                    }
                }

                previousZoomLevel = cameraPosition.zoom;
            }
        });

        this.googleMap.setOnGroundOverlayClickListener(new GoogleMap.OnGroundOverlayClickListener() {
            @Override
            public void onGroundOverlayClick(GroundOverlay groundOverlay) {
                shuttleController = new ShuttleController(latLngShuttleHashMap.get(groundOverlay.getPosition()), null);
                showShuttleDetails();

                TextView routeNameText = findViewById(R.id.route);
                TextView licenseText = findViewById(R.id.licence);

                Route route = routesController.getRoute(shuttleController.getShuttleRouteId());
                if(route != null){
                    routeNameText.setText(route.getName());
                }else{
                    routeNameText.setText("Unknown route");
                }

                licenseText.setText(shuttleController.getShuttleLicensePlateNo());

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(groundOverlay.getPosition()));
            }
        });

        mapReady = true;

        if (dataLoaded) {
            populateMap();
        }
    }

    @Override
    public void update(Model model) {
        if(!routesController.getRoutes().isEmpty() && !shuttlesController.getShuttles().isEmpty()) {
            dataLoaded = true;

            if (mapReady) {
                populateMap();
            }
        }
    }

    private void populateMap(){

        googleMap.clear();
        groundOverlays.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        boolean hasShuttles = false;

        for(Shuttle shuttle: shuttlesController.getShuttles()){

            if((shuttle.isOnDuty() && routeIndex == -1) ||
                    (shuttle.isOnDuty() && shuttle.getRouteId().equals(routesController.getRoutes().get(routeIndex).getId()))) {

                LatLng latLng = new LatLng(shuttle.getLocation().getLatitude(), shuttle.getLocation().getLongitude());

                groundOverlays.add(googleMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(Utils.bitmapDescriptorFromVector(this, R.drawable.shuttle))
                        .bearing(shuttle.getRotation())
                        .position(latLng,20)
                        .clickable(true)));

                latLngShuttleHashMap.put(latLng, shuttle);

                builder.include(latLng);

                hasShuttles = true;

                if(shuttleController != null && shuttleController.getModel().getId().equals(shuttle.getId())){
                    shuttleController = new ShuttleController(shuttle, null);
                    updateEta();
                }
            }
        }

        if(hasShuttles) {

            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            this.googleMap.animateCamera(cu);
        }else{
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.content_frame),
                    "No shuttles are available for this route.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Dismiss", new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
            hideShuttleDetails();
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
        getMenuInflater().inflate(R.menu.student_menu, menu);
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
            case R.id.view_as_list:
                startActivity(new Intent(this, ViewShuttles.class));
                break;
            default:
                toggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
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
                this.type.setText("Student");
            }
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

    public void hideShuttleDetails(){
        etaTimer = null;
        shuttleDetails.animate().translationY(Utils.convertDpToPixel(40, getApplicationContext()))
                .setInterpolator(new AccelerateInterpolator());
        filter.animate().translationY(Utils.convertDpToPixel(40, getApplicationContext()))
                .setInterpolator(new AccelerateInterpolator());;
        TextView routeNameText = findViewById(R.id.route);
        TextView licenseText = findViewById(R.id.licence);
        routeNameText.setText("No shuttle selected");
        licenseText.setText("");
        shuttleDetailsVisible = false;
    }

    public void showShuttleDetails(){
        shuttleDetails.animate().translationY(0).setInterpolator(new AccelerateInterpolator());;
        filter.animate().translationY(0).setInterpolator(new AccelerateInterpolator());
        shuttleDetailsVisible =true;

        updateEta();
    }
}
