package edu.uwi.sta.srts.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.controllers.ShuttlesController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.Utils;

public class DriverSetup extends AppCompatActivity implements View{

    private TextView toolbarText;
    private LinearLayout shuttlesLayout, routesLayout;

    private ArrayList<RadioButton> shuttleRadioButtons = new ArrayList<>();
    private ArrayList<RadioButton> routeRadioButtons = new ArrayList<>();

    private UserController driverController;
    private ShuttleController shuttleController;
    private RouteController routeController;
    private RoutesController routesController;

    private int index = 0;
    private boolean isOnDuty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_setup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarText = (TextView) findViewById(R.id.toolbarText);

        shuttlesLayout = (LinearLayout)findViewById(R.id.shuttlesLayout);
        routesLayout = findViewById(R.id.routesLayout);

        new ShuttlesController(new Shuttles(), this);
        routesController = new RoutesController(new Routes(), this);

        driverController = new UserController((User)getIntent().getSerializableExtra("user"), this);

        final ViewFlipper viewFlipper = findViewById(R.id.viewFlipper);
        final TextView next = findViewById(R.id.next);
        final TextView back = findViewById(R.id.back);

        next.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(index < 2){
                    index++;
                    viewFlipper.setDisplayedChild(index);

                    back.setVisibility(android.view.View.VISIBLE);

                    if(index == 2){
                        next.setText("Done");
                        next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_done_black_18, 0);
                    }
                }else{
                    if(getCallingActivity()==null) {
                        Intent intent = new Intent(DriverSetup.this, DriverOverview.class);
                        intent.putExtra("user", driverController.getModel());
                        intent.putExtra("shuttle", shuttleController.getModel());
                        intent.putExtra("route", routeController.getModel());
                        intent.putExtra("onDuty", isOnDuty);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("user", driverController.getModel());
                        intent.putExtra("shuttle", shuttleController.getModel());
                        intent.putExtra("route", routeController.getModel());
                        intent.putExtra("onDuty", isOnDuty);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }

                updateProgressLayout(index);
            }
        });

        back.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                index--;
                viewFlipper.setDisplayedChild(index);
                if(index==0){
                    back.setVisibility(android.view.View.GONE);
                }
                updateProgressLayout(index);
                next.setText("Next");
                next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_right_24, 0);
            }
        });

        final CardView onDuty = findViewById(R.id.onDuty);
        final CardView offDuty = findViewById(R.id.offDuty);
        onDuty.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                isOnDuty = true;
                onDuty.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                offDuty.setCardBackgroundColor(Color.parseColor("#eeeeee"));
                ((ImageView)findViewById(R.id.onImg)).setColorFilter(android.R.color.white);
                ((ImageView)findViewById(R.id.offImg)).setColorFilter(Color.parseColor("#aaaaaa"));
                ((TextView)findViewById(R.id.onText)).setTextColor(getResources().getColor(android.R.color.white));
                ((TextView)findViewById(R.id.offText)).setTextColor(Color.parseColor("#aaaaaa"));

                onDuty.setCardElevation(Utils.convertDpToPixel(12, DriverSetup.this));
                offDuty.setCardElevation(0);
            }
        });

        offDuty.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                isOnDuty = false;
                offDuty.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                onDuty.setCardBackgroundColor(Color.parseColor("#eeeeee"));
                ((ImageView)findViewById(R.id.onImg)).setColorFilter(Color.parseColor("#aaaaaa"));
                ((ImageView)findViewById(R.id.offImg)).setColorFilter(android.R.color.white);
                ((TextView)findViewById(R.id.offText)).setTextColor(getResources().getColor(android.R.color.white));
                ((TextView)findViewById(R.id.onText)).setTextColor(Color.parseColor("#aaaaaa"));

                offDuty.setCardElevation(Utils.convertDpToPixel(12, DriverSetup.this));
                onDuty.setCardElevation(0);
            }
        });
    }

    private void updateProgressLayout(int index){
        int[] attrs = new int[] { android.R.attr.textColorSecondary };
        TypedArray a = getTheme().obtainStyledAttributes(R.style.AppTheme, attrs);
        int defaultColor = a.getColor(0, Color.RED);
        a.recycle();

        CircleImageView circle1 = findViewById(R.id.circle1);
        CircleImageView circle2 = findViewById(R.id.circle2);
        CircleImageView circle3 = findViewById(R.id.circle3);
        android.view.View link1 = findViewById(R.id.link1);
        android.view.View link2 = findViewById(R.id.link2);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.text3);

        switch (index){
            default:
            case 0:
                toolbarText.setText("Choose Shuttle");
                circle1.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary, null)));
                circle2.setImageDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
                circle3.setImageDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
                link1.setBackgroundColor(Color.parseColor("#eeeeee"));
                link2.setBackgroundColor(Color.parseColor("#eeeeee"));
                text2.setTextColor(defaultColor);
                text3.setTextColor(defaultColor);
                break;
            case 1:
                toolbarText.setText("Choose Route");
                circle1.setImageDrawable(new ColorDrawable(Color.parseColor("#4DB6AC")));
                circle2.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary, null)));
                circle3.setImageDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
                link1.setBackgroundColor(Color.parseColor("#4DB6AC"));
                link2.setBackgroundColor(Color.parseColor("#eeeeee"));
                text2.setTextColor(getResources().getColor(android.R.color.white));
                text3.setTextColor(defaultColor);
                break;
            case 2:
                toolbarText.setText("Choose Status");
                circle1.setImageDrawable(new ColorDrawable(Color.parseColor("#80CBC4")));
                circle2.setImageDrawable(new ColorDrawable(Color.parseColor("#4DB6AC")));
                circle3.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary, null)));
                link1.setBackgroundColor(Color.parseColor("#80CBC4"));
                link2.setBackgroundColor(Color.parseColor("#4DB6AC"));
                text2.setTextColor(getResources().getColor(android.R.color.white));
                text3.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public void update(Model model) {
        if(model instanceof Shuttles){
            LayoutInflater inflater = getLayoutInflater();
            shuttlesLayout.removeAllViews();
            shuttleRadioButtons.clear();

            for(final Shuttle shuttle: ((Shuttles)model).getShuttles()){
                android.view.View view = inflater.inflate(R.layout.selectable_list_tem, null);
                TextView title = (TextView)view.findViewById(R.id.title);
                title.setText(shuttle.getLicensePlateNo());
                final RadioButton radioButton = view.findViewById(R.id.radioButton);

                if(shuttle.getDriverId().equals(driverController.getUserId())){
                    radioButton.setChecked(true);
                    shuttleController = new ShuttleController(shuttle, null);

                    if(routesController!= null){
                        routesController.updateView();
                    }
                }

                shuttleRadioButtons.add(radioButton);

                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            for(RadioButton r : shuttleRadioButtons){
                                if(!r.equals(buttonView)) {
                                    r.setChecked(false);
                                }
                            }

                            shuttleController = new ShuttleController(shuttle, null);
                        }else{
                            boolean anyChecked =false;
                            for(RadioButton r : shuttleRadioButtons){
                                if(r.isChecked()){
                                    anyChecked=true;
                                    break;
                                }
                            }
                            if(!anyChecked){
                                buttonView.setChecked(true);
                            }
                        }
                    }
                });

                view.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        radioButton.setChecked(!radioButton.isChecked());
                    }
                });

                shuttlesLayout.addView(view);
            }
            boolean anyChecked =false;
            for(RadioButton r : shuttleRadioButtons){
                if(r.isChecked()){
                    anyChecked=true;
                    break;
                }
            }
            if(!anyChecked && shuttleRadioButtons.size() != 0){
                shuttleRadioButtons.get(0).setChecked(true);
            }
        }else if(model instanceof Routes){
            LayoutInflater inflater = getLayoutInflater();
            routesLayout.removeAllViews();
            routeRadioButtons.clear();

            for(final Route route: ((Routes)model).getRoutes()){
                android.view.View view = inflater.inflate(R.layout.selectable_list_tem, null);
                TextView title = (TextView)view.findViewById(R.id.title);
                title.setText(route.getName());
                final RadioButton radioButton = view.findViewById(R.id.radioButton);

                if(shuttleController != null && shuttleController.getShuttleRouteId().equals(route.getId())){
                    radioButton.setChecked(true);
                    routeController = new RouteController(route, null);
                }

                routeRadioButtons.add(radioButton);

                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            for(RadioButton r : routeRadioButtons){
                                if(!r.equals(buttonView)) {
                                    r.setChecked(false);
                                }
                            }
                            routeController = new RouteController(route, null);
                        }else{
                            boolean anyChecked =false;
                            for(RadioButton r : routeRadioButtons){
                                if(r.isChecked()){
                                    anyChecked=true;
                                    break;
                                }
                            }
                            if(!anyChecked){
                                buttonView.setChecked(true);
                            }
                        }
                    }
                });

                view.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        radioButton.setChecked(!radioButton.isChecked());
                    }
                });

                routesLayout.addView(view);
            }
            boolean anyChecked =false;
            for(RadioButton r : routeRadioButtons){
                if(r.isChecked()){
                    anyChecked=true;
                    break;
                }
            }
            if(!anyChecked && routeRadioButtons.size() != 0){
                routeRadioButtons.get(0).setChecked(true);
            }
        }
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
}
