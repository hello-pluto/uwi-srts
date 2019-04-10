package edu.uwi.sta.srts.views.admin;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.HashMap;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RouteStopsController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.StopsController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.views.View;

public class EditRoute extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private RouteController routeController;

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout frequencyTextInputLayout;

    private static HashMap<String, RouteStop> routeStopHashMap = new HashMap<>();

    private StopsView stopsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);

        routeStopHashMap.clear();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Route route = (Route) getIntent().getSerializableExtra("route");

        if(route == null){
            route = new Route();
            isEditMode = false;
        }

        routeController = new RouteController(route, this);

        Button done = (Button) findViewById(R.id.done);
        nameTextInputLayout = (TextInputLayout)findViewById(R.id.nameLayout);
        frequencyTextInputLayout = (TextInputLayout)findViewById(R.id.frequencyLayout);

        stopsView = new StopsView((LinearLayout) findViewById(R.id.stopsLayout));

        if(isEditMode){
            update(route);
            getSupportActionBar().setTitle("Edit Route");
            new RouteStopsController(new RouteStops(), this);

            new StopsController(new Stops(), stopsView);
        }else{
            getSupportActionBar().setTitle("Add Route");
            findViewById(R.id.stopsLayout).setVisibility(android.view.View.GONE);
            findViewById(R.id.stopsDivider).setVisibility(android.view.View.GONE);
            findViewById(R.id.stopsText).setVisibility(android.view.View.GONE);
        }

        nameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    nameTextInputLayout.setError("Enter a route name");
                }else{
                    nameTextInputLayout.setError(null);
                    routeController.setRouteName(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        frequencyTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    routeController.setRouteFrequency(Integer.valueOf(s.toString()));
                }else{
                    routeController.setRouteFrequency(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(nameTextInputLayout.getError() == null){
                    routeController.saveModel();
                    for(RouteStop routeStop : routeStopHashMap.values()){
                        routeStop.save();
                    }
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
        if(model instanceof Route){
            nameTextInputLayout.getEditText().setText(((Route)model).getName());
            frequencyTextInputLayout.getEditText().setText(String.valueOf(((Route)model).getFrequency()));
        }else if(model instanceof RouteStops){
            ((RouteStops)model).filterSelf(routeController.getRouteId());
            for(RouteStop routeStop : ((RouteStops)model).getRouteStops()){
                routeStopHashMap.put(routeStop.getStopId(), routeStop);
            }

            stopsView.update(new Stops());
        }
    }

    public class StopsView implements edu.uwi.sta.srts.views.View {

        LinearLayout layout;

        public StopsView(LinearLayout layout){
            this.layout = layout;
        }

        @Override
        public void update(Model model) {
            if(model instanceof Stops && layout != null){
                for(final Stop stop: ((Stops)model).getStops()) {
                    CheckBox checkBox = (CheckBox) getLayoutInflater().inflate(R.layout.list_checkbox, null);
                    checkBox.setText(stop.getName());
                    if(routeStopHashMap.containsKey(stop.getId())){
                        checkBox.setChecked(true);
                    }
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                routeStopHashMap.put(stop.getId(), new RouteStop(routeController.getRouteId(), stop.getId()));
                            }else{
                                routeStopHashMap.get(stop.getId()).delete();
                                routeStopHashMap.remove(stop.getId());
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        }
    }
}
