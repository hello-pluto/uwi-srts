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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.InfoDialogHelper;
import edu.uwi.sta.srts.utils.Utils;
import edu.uwi.sta.srts.views.fragments.AlertsFragment;
import edu.uwi.sta.srts.views.fragments.DriversFragment;
import edu.uwi.sta.srts.views.fragments.RoutesFragment;
import edu.uwi.sta.srts.views.fragments.ShuttlesFragment;
import edu.uwi.sta.srts.views.fragments.StopsFragment;

public class AdminOverview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private Fragment fragment;
    private TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarText = findViewById(R.id.toolbarText);
        toolbarText.setText("Routes");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(fragment instanceof DriversFragment){
                    intent = new Intent(AdminOverview.this, EditUser.class);
                }else if(fragment instanceof RoutesFragment){
                    intent = new Intent(AdminOverview.this, EditRoute.class);
                }else if(fragment instanceof ShuttlesFragment){
                    intent = new Intent(AdminOverview.this, EditShuttle.class);
                }else if(fragment instanceof StopsFragment){
                    intent = new Intent(AdminOverview.this, EditStop.class);
                }else if(fragment instanceof AlertsFragment) {
                    intent = new Intent(AdminOverview.this, EditAlert.class);
                }

                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = RoutesFragment.newInstance(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        Utils.setupOfflineSnackbarListener(findViewById(R.id.content_frame));

        View headerLayout = navigationView.getHeaderView(0);

        TextView navName = headerLayout.findViewById(R.id.navName);
        TextView navEmail = headerLayout.findViewById(R.id.navEmail);
        TextView navType = headerLayout.findViewById(R.id.navType);
        FloatingActionButton navEditUser = headerLayout.findViewById(R.id.edit_user);
        navEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOverview.this, EditUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOverview.this, ViewUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                intent.putExtra("isAdmin", false);
                startActivity(intent);
            }
        });

        try {
            new UserController(new User(FirebaseAuth.getInstance().getCurrentUser().getUid()), new UserView(navName, navType, navEmail));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (id) {
            case R.id.nav_routes:
                fragment = RoutesFragment.newInstance(true);
                toolbarText.setText("Routes");
                break;
            case R.id.nav_drivers:
                fragment = DriversFragment.newInstance();
                toolbarText.setText("Drivers");
                break;
            case R.id.nav_shuttles:
                fragment = ShuttlesFragment.newInstance(true);
                toolbarText.setText("Shuttles");
                break;
            case R.id.nav_stops:
                fragment = StopsFragment.newInstance(true);
                toolbarText.setText("Stops");
                break;
            case R.id.nav_alerts:
                fragment = AlertsFragment.newInstance(true);
                toolbarText.setText("Alerts");
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

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
            case R.id.help:
                if(fragment instanceof RoutesFragment) {
                    InfoDialogHelper.showInfoDialog(this,
                            "Routes",
                            "Click on the green button on the bottom right to add a new " +
                                    "route or click on a route item in the list to view it in more detail.");
                }else if(fragment instanceof DriversFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Drivers",
                            "Click on the green button on the bottom right to add a new " +
                                    "driver or click on a driver item in the list to view it in more detail.");
                }else if(fragment instanceof ShuttlesFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Shuttles",
                            "Click on the green button on the bottom right to add a new " +
                                    "shuttle or click on a shuttle item in the list to view it in more detail.");
                }else if(fragment instanceof StopsFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Stops",
                            "Click on the green button on the bottom right to add a new " +
                                    "stop or click on a stop item in the list to view it in more detail.");
                }else if(fragment instanceof AlertsFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Alerts",
                            "Click on the green button on the bottom right to add a new " +
                                    "alert or click on a alert item in the list to view it in more detail.");
                }
                return true;
            default:
                toggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public class UserView implements edu.uwi.sta.srts.utils.View {

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
                this.type.setText("Administrator");
            }
        }
    }
}
