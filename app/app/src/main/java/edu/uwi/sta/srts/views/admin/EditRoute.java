package edu.uwi.sta.srts.views.admin;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.views.View;

public class EditRoute extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private RouteController routeController;

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout frequencyTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);

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

        if(isEditMode){
            update(route);
            getSupportActionBar().setTitle("Edit Route");
        }else{
            getSupportActionBar().setTitle("Add Route");
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
        }
    }
}
