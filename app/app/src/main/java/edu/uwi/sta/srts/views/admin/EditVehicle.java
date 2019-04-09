package edu.uwi.sta.srts.views.admin;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.VehicleController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Vehicle;
import edu.uwi.sta.srts.views.View;

public class EditVehicle extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private VehicleController vehicleController;

    private TextInputLayout plateNoTextInputLayout;
    private TextInputLayout capacityTextInputLayout;

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

        if(isEditMode){
            update(vehicle);
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
    public void update(Model model) {
        if(model instanceof Vehicle){
            plateNoTextInputLayout.getEditText().setText(((Vehicle)model).getLicensePlateNo());
            capacityTextInputLayout.getEditText().setText(String.valueOf(((Vehicle)model).getCapacity()));
        }
    }
}
