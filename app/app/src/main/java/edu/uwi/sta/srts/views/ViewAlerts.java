package edu.uwi.sta.srts.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.models.Model;

public class ViewAlerts extends AppCompatActivity implements View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alerts);
    }

    @Override
    public void update(Model model) {

    }
}
