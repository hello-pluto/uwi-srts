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
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.UsersController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.utils.UserType;
import edu.uwi.sta.srts.utils.Utils;

public class EditShuttle extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private ShuttleController shuttleController;

    private EditText licensePlateEditText;
    private EditText capacityEditText;

    private android.view.View driversLayout;
    private android.view.View routesLayout;

    private TextView driverText;
    private TextView routeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shuttle);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        TextView toolbarText = (TextView)findViewById(R.id.toolbarText);

        Shuttle shuttle = (Shuttle) getIntent().getSerializableExtra("shuttle");

        if(shuttle == null){
            shuttle = new Shuttle();
            isEditMode = false;
        }

        shuttleController = new ShuttleController(shuttle, this);

        android.view.View done = findViewById(R.id.done);
        licensePlateEditText =  findViewById(R.id.licensePlate);
        capacityEditText = findViewById(R.id.capacity);

        final TextView licensePlateError = findViewById(R.id.licensePlateError);
        final TextView capacityError = findViewById(R.id.capacityError);

        driversLayout = findViewById(R.id.driverLayout);
        driverText = findViewById(R.id.driver);
        routeText = findViewById(R.id.route);
        routesLayout = findViewById(R.id.routeLayout);

        Utils.setUpActivations(this, licensePlateEditText, findViewById(R.id.licensePlateUnderline));
        Utils.setUpActivations(this, capacityEditText, findViewById(R.id.capacityUnderline));


        if(isEditMode){
            update(shuttle);
            new UserController(new User(shuttleController.getShuttleDriverId()), this);
            new RouteController(new Route(shuttleController.getShuttleRouteId()), this);
            toolbarText.setText("Edit Shuttle");
            driversLayout.setVisibility(android.view.View.VISIBLE);
            routesLayout.setVisibility(android.view.View.VISIBLE);
        }else{
            toolbarText.setText("Add Shuttle");
        }

        licensePlateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = shuttleController.setShuttleLicensePlateNo(s.toString());
                if(error == null){
                    findViewById(R.id.licensePlateDone).setVisibility(android.view.View.VISIBLE);
                }else{
                    licensePlateError.setText(error);
                    findViewById(R.id.licensePlateDone).setVisibility(android.view.View.GONE);
                }

                if(licensePlateError.getVisibility() == android.view.View.VISIBLE){
                    licensePlateError.setVisibility(android.view.View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        capacityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    findViewById(R.id.capacityDone).setVisibility(android.view.View.VISIBLE);
                    shuttleController.setShuttleCapacity(Integer.valueOf(s.toString()));
                }else{
                    findViewById(R.id.capacityDone).setVisibility(android.view.View.GONE);
                    shuttleController.setShuttleCapacity(0);
                    capacityError.setText("Enter a capacity");
                }

                if(capacityError.getVisibility() == android.view.View.VISIBLE){
                    capacityError.setVisibility(android.view.View.INVISIBLE);
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

                if(findViewById(R.id.licensePlateDone).getVisibility() != android.view.View.VISIBLE) {
                    licensePlateError.setVisibility(android.view.View.VISIBLE);
                    findViewById(R.id.licensePlateUnderline).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    error = true;
                }

                if(findViewById(R.id.capacityDone).getVisibility() != android.view.View.VISIBLE) {
                    capacityError.setVisibility(android.view.View.VISIBLE);
                    findViewById(R.id.capacityUnderline).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    error = true;
                }

                if(!error){
                    shuttleController.saveModel();
                    finish();
                }
            }
        });

        Users drivers = new Users();
        drivers.filter(UserType.DRIVER);

        new UsersController(drivers, this);
        new RoutesController(new Routes(), this);
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
    public void update(final Model model) {
        if(model instanceof Shuttle){
            licensePlateEditText.setText(((Shuttle)model).getLicensePlateNo());
            capacityEditText.setText(String.valueOf(((Shuttle)model).getCapacity()));

            findViewById(R.id.licensePlateDone).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.capacityDone).setVisibility(android.view.View.VISIBLE);
        }else if(model instanceof Users){
            final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(this, driversLayout);
            menu.getMenuInflater()
                    .inflate(R.menu.default_menu, menu.getMenu());

            for(User driver : ((Users)model).getUsers()){
                menu.getMenu().add(R.id.default_group, driver.hashCode(), Menu.NONE, driver.getFullName());
            }

            menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId() == 0){
                        driverText.setText("None");
                        shuttleController.setShuttleDriverId("");
                        return true;
                    }else if(menuItem.getItemId() == 1){
                        Intent intent = new Intent(EditShuttle.this, EditUser.class);
                        startActivity(intent);
                        return true;
                    }else {
                        for (User driver : ((Users)model).getUsers()) {
                            if (driver.hashCode() == menuItem.getItemId()) {
                                shuttleController.setShuttleDriverId(driver.getId());
                                driverText.setText(driver.getFullName());
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

            menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "None");
            menu.getMenu().add(R.id.add_group, 1, Menu.NONE, "Add Driver");

            driversLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    menu.show();
                }
            });

            MenuCompat.setGroupDividerEnabled(menu.getMenu(), true);
        }else if (model instanceof User){
            driverText.setText(((User)model).getFullName());
        }else if(model instanceof Routes){
            final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(this, routesLayout);
            menu.getMenuInflater()
                    .inflate(R.menu.default_menu, menu.getMenu());

            for(Route route : ((Routes)model).getRoutes()){
                menu.getMenu().add(R.id.default_group, route.hashCode(), Menu.NONE, route.getName());
            }

            menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId() == 0){
                        routeText.setText("None");
                        shuttleController.setShuttleRouteId("");
                        return true;
                    }else if(menuItem.getItemId() == 1){
                        Intent intent = new Intent(EditShuttle.this, EditRoute.class);
                        startActivity(intent);
                        return true;
                    }else {
                        for (Route route : ((Routes)model).getRoutes()) {
                            if (route.hashCode() == menuItem.getItemId()) {
                                shuttleController.setShuttleRouteId(route.getId());
                                routeText.setText(route.getName());
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

            menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "None");
            menu.getMenu().add(R.id.add_group, 1, Menu.NONE, "Add Route");

            routesLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    menu.show();
                }
            });

            MenuCompat.setGroupDividerEnabled(menu.getMenu(), true);
        }else if (model instanceof Route){
            routeText.setText(((Route)model).getName());
        }
    }
}
