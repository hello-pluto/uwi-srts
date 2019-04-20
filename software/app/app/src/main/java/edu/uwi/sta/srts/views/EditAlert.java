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

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.AlertController;
import edu.uwi.sta.srts.models.Alert;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.utils.Utils;

public class EditAlert extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private AlertController alertController;

    private EditText titleEditText;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alert);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        TextView toolbarText = (TextView)findViewById(R.id.toolbarText);

        Alert alert = (Alert) getIntent().getSerializableExtra("alert");

        if(alert == null){
            alert = new Alert();
            isEditMode = false;
        }

        alertController = new AlertController(alert, this);

        android.view.View done = findViewById(R.id.done);
        titleEditText = (EditText) findViewById(R.id.title);
        messageEditText = (EditText) findViewById(R.id.message);

        Utils.setUpActivations(this, titleEditText, findViewById(R.id.titleUnderline));
        Utils.setUpActivations(this, messageEditText, findViewById(R.id.messageUnderline));

        if(isEditMode){
            update(alert);
            toolbarText.setText("Edit Alert");
        }else{
            toolbarText.setText("Add Alert");
        }

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    titleEditText.setError(null);
                    alertController.setAlertTitle(s.toString());
                }else{
                    titleEditText.setError("Enter an alert title");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alertController.setAlertMessage(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(titleEditText.getError() == null){
                    alertController.saveModel();
                    finish();
                }
            }
        });

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

        setOnClickListener(findViewById(R.id.circle1), 1);
        setOnClickListener(findViewById(R.id.circle2), 2);
        setOnClickListener(findViewById(R.id.circle3), 3);
        setOnClickListener(findViewById(R.id.circle4), 4);
        setOnClickListener(findViewById(R.id.circle5), 5);
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
        if (model instanceof Alert) {
            titleEditText.setText(alertController.getAlertTitle());
            messageEditText.setText(alertController.getAlertMessage());

            findViewById(R.id.titleDone).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.messageDone).setVisibility(android.view.View.VISIBLE);

            findViewById(R.id.outline1).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline2).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline3).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline4).setVisibility(android.view.View.INVISIBLE);
            findViewById(R.id.outline5).setVisibility(android.view.View.INVISIBLE);

            switch (alertController.getAlertUrgency()){
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

    public void setOnClickListener(android.view.View v, final int index){
        v.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                alertController.setAlertUrgency(index);
                alertController.update();
            }
        });
    }
}
