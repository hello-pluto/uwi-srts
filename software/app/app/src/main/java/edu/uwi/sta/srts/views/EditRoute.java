/*
 * Copyright (c) 2019. Razor Sharp Software Solutions
 *
 * Azel Daniel (816002285)
 * Michael Bristol (816003612)
 * Amanda Seenath (816002935)
 *
 * INFO 3604
 * Project
 *
 * UWI Shuttle Routing and Tracking System
 */

package edu.uwi.sta.srts.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RouteStopsController;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.controllers.StopsController;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.utils.Utils;
import edu.uwi.sta.srts.utils.View;

public class EditRoute extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private RouteController routeController;

    private EditText nameEditText;
    private EditText frequencyEditText;

    private TextView nameError;
    private TextView frequencyError;

    private int nextOrder = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        TextView toolbarText = findViewById(R.id.toolbarText);

        Route route = (Route) getIntent().getSerializableExtra("route");

        if(route == null){
            route = new Route();
            isEditMode = false;
        }

        routeController = new RouteController(route, this);

        android.view.View done = findViewById(R.id.done);
        nameEditText =  findViewById(R.id.name);
        frequencyEditText =  findViewById(R.id.frequency);

        nameError = findViewById(R.id.nameError);
        frequencyError = findViewById(R.id.frequencyError);

        Utils.setUpActivations(this, nameEditText, findViewById(R.id.nameUnderline));
        Utils.setUpActivations(this, frequencyEditText, findViewById(R.id.frequencyUnderline));

        if(isEditMode){
            update(route);
            toolbarText.setText("Edit Route");
            new RouteStopsController(new RouteStops(), this);

            new StopsController(new Stops(), this);
        }else{
            toolbarText.setText("Add Route");
            findViewById(R.id.stopsLayout).setVisibility(android.view.View.GONE);
        }

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    nameError.setText("Enter a route name");
                    findViewById(R.id.nameDone).setVisibility(android.view.View.GONE);
                }else{
                    findViewById(R.id.nameDone).setVisibility(android.view.View.VISIBLE);
                    routeController.setRouteName(s.toString());
                }

                if(nameError.getVisibility() == android.view.View.VISIBLE){
                    nameError.setVisibility(android.view.View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        frequencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    findViewById(R.id.frequencyDone).setVisibility(android.view.View.VISIBLE);
                    routeController.setRouteFrequency(Integer.valueOf(s.toString()));
                }else{
                    findViewById(R.id.frequencyDone).setVisibility(android.view.View.GONE);
                    routeController.setRouteFrequency(0);
                    frequencyError.setText("Enter a frequency");
                }

                if(frequencyError.getVisibility() == android.view.View.VISIBLE){
                    frequencyError.setVisibility(android.view.View.INVISIBLE);
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

                if(findViewById(R.id.nameDone).getVisibility() != android.view.View.VISIBLE) {
                    nameError.setVisibility(android.view.View.VISIBLE);
                    findViewById(R.id.nameUnderline).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    error = true;
                }

                if(findViewById(R.id.frequencyDone).getVisibility() != android.view.View.VISIBLE) {
                    frequencyError.setVisibility(android.view.View.VISIBLE);
                    findViewById(R.id.frequencyUnderline).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    error = true;
                }
                if(!error) {
                    routeController.saveModel();
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
    public void update(final Model model) {
        if(model instanceof Route){
            nameEditText.setText(((Route)model).getName());
            frequencyEditText.setText(String.valueOf(((Route)model).getFrequency()));

            findViewById(R.id.nameDone).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.frequencyDone).setVisibility(android.view.View.VISIBLE);
        }else if(model instanceof RouteStops){
            ((RouteStops)model).filter(routeController.getRouteId());
            redrawStops((RouteStops)model);
        }else if(model instanceof Stops){
            findViewById(R.id.addStop).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    PopupMenu menu = new PopupMenu(EditRoute.this, findViewById(R.id.addStop));

                    for(Stop stop : ((Stops)model).getStops()){
                        menu.getMenu().add(Menu.NONE, stop.hashCode(), 0, stop.getName());
                    }

                    menu.show();

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            for(Stop stop : ((Stops)model).getStops()){
                                if(stop.hashCode() == item.getItemId()){
                                    RouteStop rs = new RouteStop();
                                    rs.setRouteId(routeController.getRouteId());
                                    rs.setStopId(stop.getId());
                                    rs.setOrder(nextOrder);
                                    rs.save();
                                    return true;
                                }
                            }
                            return false;
                        }

                    });
                }
            });
        }
    }

    public void redrawStops(final RouteStops model){

        LinearLayout ll = findViewById(R.id.stopsLayoutAdd);
        ll.removeAllViews();

        nextOrder = 1;

        for(final RouteStop routeStop : model.getRouteStops()) {

            nextOrder++;

            LayoutInflater inflater = getLayoutInflater();
            android.view.View view = inflater.inflate(R.layout.route_stop_list_item, null);

            TextView stopName = view.findViewById(R.id.title);

            TextView order = view.findViewById(R.id.order);
            order.setText(String.valueOf(routeStop.getOrder()));

            ImageButton remove = view.findViewById(R.id.remove);
            remove.setVisibility(android.view.View.VISIBLE);
            remove.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    for(RouteStop rs : model.getRouteStops()){
                        if(rs.getOrder() > routeStop.getOrder()){
                            rs.setOrder(rs.getOrder()-1);
                            rs.save();
                        }
                    }
                    routeStop.delete();
                }
            });

            if (routeStop.getOrder() == 1) {
                view.findViewById(R.id.above).setVisibility(android.view.View.GONE);
            }

            if (routeStop.getOrder() == ((RouteStops) model).getRouteStops().size()) {
                view.findViewById(R.id.below).setVisibility(android.view.View.GONE);
            }

            new StopController(new Stop(routeStop.getStopId()), new StopView(stopName));

            ll.addView(view);
        }
    }

    public class StopView implements View {

        TextView textView;

        private StopView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof Stop && textView != null){
                this.textView.setText(((Stop)model).getName());
            }
        }
    }
}
