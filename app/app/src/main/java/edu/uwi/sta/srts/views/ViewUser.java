package edu.uwi.sta.srts.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.models.Model;

public class ViewUser extends AppCompatActivity implements View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
    }

    @Override
    public void update(Model model) {

    }
}
