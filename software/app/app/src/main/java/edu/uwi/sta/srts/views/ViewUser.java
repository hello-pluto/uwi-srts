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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.View;

public class ViewUser extends AppCompatActivity implements View {

    private UserController userController;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        User user = (User)getIntent().getSerializableExtra("user");

        userController = new UserController(new User(user.getId()), this);
    }

    @Override
    public void update(Model model) {
        if (model instanceof User){
            loaded = true;
            TextView email = findViewById(R.id.email);
            email.setText(userController.getUserEmail());

            TextView fullName = findViewById(R.id.fullname);
            fullName.setText(userController.getUserFullName());

            TextView userType = findViewById(R.id.userType);
            switch (userController.getUserType()){
                case ADMINISTRATOR:
                    userType.setText(getString(R.string.administrator));
                    break;
                default:
                case STUDENT:
                    userType.setText(getString(R.string.student));
                    break;
                case DRIVER:
                    userType.setText(getString(R.string.driver));
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loaded){
            userController.update();
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
                    Intent intent = new Intent(this, EditUser.class);
                    intent.putExtra("user", userController.getModel());
                    startActivity(intent);
                }
                return true;
            case R.id.nav_delete:
                userController.getModel().delete();
                finish();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
