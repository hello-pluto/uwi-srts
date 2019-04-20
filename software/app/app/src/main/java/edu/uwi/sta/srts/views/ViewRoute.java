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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RouteStopsController;
import edu.uwi.sta.srts.controllers.ShuttlesController;
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.RouteStop;
import edu.uwi.sta.srts.models.RouteStops;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.models.Stop;

public class ViewRoute extends AppCompatActivity implements View {

    private RouteController routeController;
    private LinearLayout ll;

    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Route route = (Route) getIntent().getSerializableExtra("route");

        routeController = new RouteController(new Route(route.getId()), this);

        ll = findViewById(R.id.stopsLayoutAdd);
    }

    @Override
    public void update(Model model) {
        if(model instanceof Route){
            loaded = true;
            TextView name = findViewById(R.id.name);
            name.setText(routeController.getRouteName());

            TextView frequency = findViewById(R.id.frequency);
            frequency.setText("Every " + ((Route)model).getFrequency() + " minutes");

            RouteStops routeStops = new RouteStops();
            routeStops.filter(routeController.getRouteId());

            new RouteStopsController(routeStops, this);

            new ShuttlesController(new Shuttles(routeController.getRouteId()),
                    new ViewRoute.ShuttlesView((TextView)findViewById(R.id.shuttleCount)));

        }else if(model instanceof RouteStops){
            if(((RouteStops)model).getRouteStops().size() > 0) {
                ll.removeAllViews();
                for (RouteStop routeStop : ((RouteStops) model).getRouteStops()) {
                    LayoutInflater inflater = getLayoutInflater();
                    android.view.View view = inflater.inflate(R.layout.route_stop_list_item, null);

                    TextView stopName = (TextView) view.findViewById(R.id.title);

                    TextView order = (TextView) view.findViewById(R.id.order);
                    order.setText(String.valueOf(routeStop.getOrder()));

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
        }
    }

    public class StopView implements edu.uwi.sta.srts.views.View {

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
    public class ShuttlesView implements edu.uwi.sta.srts.views.View {

        TextView textView;

        private ShuttlesView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof Shuttles && textView != null){
                int size = ((Shuttles)model).getShuttles().size();
                if(size == 0){
                    this.textView.setText("No on duty shuttles");
                }else if(size == 1){
                    this.textView.setText("1 on duty shuttle");
                }else{
                    this.textView.setText(size + " on duty shuttles");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getIntent().getBooleanExtra("isAdmin", false)) {
            getMenuInflater().inflate(R.menu.model_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_edit:
                if(loaded) {
                    Intent intent = new Intent(this, EditRoute.class);
                    intent.putExtra("route", routeController.getModel());
                    startActivity(intent);
                }
                return true;
            case R.id.nav_delete:
                routeController.getModel().delete();
                finish();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
