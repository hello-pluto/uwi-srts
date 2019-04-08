package edu.uwi.sta.srts.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.views.admin.AdminOverview;

public class SplashActivity extends AppCompatActivity implements View  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            User user = new User(firebaseAuth.getCurrentUser().getUid());
            new UserController(user, this);
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void update(Model model) {
        if(model instanceof User){
            Intent intent;
            switch (((User)model).getUserType()){
                default:
                case ADMINISTRATOR:
                    intent = new Intent(this, AdminOverview.class);
                    break;
            }
            intent.putExtra("user", (User)model);
            startActivity(intent);

            finish();
        }
    }
}
