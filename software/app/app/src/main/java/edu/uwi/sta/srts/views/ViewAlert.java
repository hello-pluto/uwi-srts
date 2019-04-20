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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.AlertController;
import edu.uwi.sta.srts.models.Alert;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.utils.Utils;

public class ViewAlert extends AppCompatActivity implements View {

    private AlertController alertController;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alert);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Alert alert = (Alert)getIntent().getSerializableExtra("alert");

        alertController = new AlertController(new Alert(alert.getId()), this);
    }

    @Override
    public void update(Model model) {
        if (model instanceof Alert){
            loaded = true;
            TextView title = findViewById(R.id.title);
            title.setText(alertController.getAlertTitle());

            TextView message = findViewById(R.id.message);
            message.setText(alertController.getAlertMessage());

            int urgency = alertController.getAlertUrgency();

            findViewById(R.id.outline1).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline2).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline3).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline4).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline5).setVisibility(android.view.View.INVISIBLE);

            ((CircleImageView)findViewById(R.id.circle1)).setImageDrawable(
                    new ColorDrawable(Utils.getUrgencyColor(1)));
            ((CircleImageView)findViewById(R.id.circle2)).setImageDrawable(
                    new ColorDrawable(Utils.getUrgencyColor(2)));
            ((CircleImageView)findViewById(R.id.circle3)).setImageDrawable(
                    new ColorDrawable(Utils.getUrgencyColor(3)));
            ((CircleImageView)findViewById(R.id.circle4)).setImageDrawable(
                    new ColorDrawable(Utils.getUrgencyColor(4)));
            ((CircleImageView)findViewById(R.id.circle5)).setImageDrawable(
                    new ColorDrawable(Utils.getUrgencyColor(5)));

            ((CircleImageView)findViewById(R.id.outline1)).setBorderColor(Utils.getUrgencyColor(1));
            ((CircleImageView)findViewById(R.id.outline2)).setBorderColor(Utils.getUrgencyColor(2));
            ((CircleImageView)findViewById(R.id.outline3)).setBorderColor(Utils.getUrgencyColor(3));
            ((CircleImageView)findViewById(R.id.outline4)).setBorderColor(Utils.getUrgencyColor(4));
            ((CircleImageView)findViewById(R.id.outline5)).setBorderColor(Utils.getUrgencyColor(5));

            switch (urgency){
                case 1:
                    findViewById(R.id.outline1).setVisibility(android.view.View.VISIBLE);
                    break;
                case 2:
                    findViewById(R.id.outline2).setVisibility(android.view.View.VISIBLE);
                    break;
                case 3:
                    findViewById(R.id.outline3).setVisibility(android.view.View.VISIBLE);
                    break;
                case 4:
                    findViewById(R.id.outline4).setVisibility(android.view.View.VISIBLE);
                    break;
                default:
                case 5:
                    findViewById(R.id.outline5).setVisibility(android.view.View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loaded){
            alertController.update();
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
                    Intent intent = new Intent(this, EditAlert.class);
                    intent.putExtra("alert", alertController.getModel());
                    startActivity(intent);
                }
                return true;
            case R.id.nav_delete:
                alertController.getModel().delete();
                finish();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
