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
import edu.uwi.sta.srts.controllers.AlertController;
import edu.uwi.sta.srts.models.Alert;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.views.View;

public class EditAlert extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private AlertController alertController;

    private TextInputLayout titleTextInputLayout;
    private TextInputLayout messageTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alert);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Alert alert = (Alert) getIntent().getSerializableExtra("alert");

        if(alert == null){
            alert = new Alert();
            isEditMode = false;
        }

        alertController = new AlertController(alert, this);

        Button done = (Button) findViewById(R.id.done);
        titleTextInputLayout = (TextInputLayout)findViewById(R.id.titleLayout);
        messageTextInputLayout = (TextInputLayout)findViewById(R.id.messageLayout);

        if(isEditMode){
            update(alert);
            getSupportActionBar().setTitle("Edit Alert");
        }else{
            getSupportActionBar().setTitle("Add Alert");
        }

        titleTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    titleTextInputLayout.setError(null);
                    alertController.setAlertTitle(s.toString());
                }else{
                    titleTextInputLayout.setError("Enter an alert title");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        messageTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
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
                if(titleTextInputLayout.getError() == null){
                    alertController.saveModel();
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
        if (model instanceof Alert) {
            titleTextInputLayout.getEditText().setText(alertController.getAlertTitle());
            messageTextInputLayout.getEditText().setText(alertController.getAlertMessage());
        }
    }
}
