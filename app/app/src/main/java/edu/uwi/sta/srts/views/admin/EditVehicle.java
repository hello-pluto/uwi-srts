package edu.uwi.sta.srts.views.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.UsersController;
import edu.uwi.sta.srts.controllers.VehicleController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.models.Vehicle;
import edu.uwi.sta.srts.models.utils.UserType;
import edu.uwi.sta.srts.views.View;

public class EditVehicle extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private VehicleController vehicleController;

    private TextInputLayout plateNoTextInputLayout;
    private TextInputLayout capacityTextInputLayout;

    private RelativeLayout driversLayout;
    private RelativeLayout routesLayout;

    private TextView driverText;
    private TextView routeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Vehicle vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");

        if(vehicle == null){
            vehicle = new Vehicle();
            isEditMode = false;
        }

        vehicleController = new VehicleController(vehicle, this);

        Button done = (Button) findViewById(R.id.done);
        plateNoTextInputLayout = (TextInputLayout)findViewById(R.id.plateNoLayout);
        capacityTextInputLayout = (TextInputLayout)findViewById(R.id.vehicleCapacityLayout);
        driversLayout = (RelativeLayout)findViewById(R.id.rl1);
        driverText = (TextView)findViewById(R.id.driver);
        routeText = (TextView)findViewById(R.id.route);
        routesLayout = (RelativeLayout)findViewById(R.id.rl2);

        if(isEditMode){
            update(vehicle);
            new UserController(new User(vehicleController.getVehicleDriverId()), this);
            new RouteController(new Route(vehicleController.getVehicleRouteId()), this);
            getSupportActionBar().setTitle("Edit Vehicle");
        }else{
            getSupportActionBar().setTitle("Add Vehicle");
        }

        plateNoTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = vehicleController.setVehicleLicensePlateNo(s.toString());
                if(error == null){
                    plateNoTextInputLayout.setError(null);
                }else{
                    plateNoTextInputLayout.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        capacityTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    vehicleController.setVehicleCapacity(Integer.valueOf(s.toString()));
                }else{
                    vehicleController.setVehicleCapacity(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(plateNoTextInputLayout.getError() == null){
                    vehicleController.saveModel();
                    finish();
                }
            }
        });

        Users drivers = new Users();
        drivers.filterSelf(UserType.DRIVER);

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
        if(model instanceof Vehicle){
            plateNoTextInputLayout.getEditText().setText(((Vehicle)model).getLicensePlateNo());
            capacityTextInputLayout.getEditText().setText(String.valueOf(((Vehicle)model).getCapacity()));
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
                        vehicleController.setVehicleDriverId("");
                        return true;
                    }else if(menuItem.getItemId() == 1){
                        Intent intent = new Intent(EditVehicle.this, EditUser.class);
                        startActivity(intent);
                        return true;
                    }else {
                        for (User driver : ((Users)model).getUsers()) {
                            if (driver.hashCode() == menuItem.getItemId()) {
                                vehicleController.setVehicleDriverId(driver.getId());
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
                        vehicleController.setVehicleRouteId("");
                        return true;
                    }else if(menuItem.getItemId() == 1){
                        Intent intent = new Intent(EditVehicle.this, EditRoute.class);
                        startActivity(intent);
                        return true;
                    }else {
                        for (Route route : ((Routes)model).getRoutes()) {
                            if (route.hashCode() == menuItem.getItemId()) {
                                vehicleController.setVehicleRouteId(route.getId());
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
