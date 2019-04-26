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

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.UserType;
import edu.uwi.sta.srts.utils.View;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class SplashActivity extends AppCompatActivity implements View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                findViewById(R.id.icon),
                PropertyValuesHolder.ofFloat("rotation", 0f, 360f));
        scaleDown.setDuration(800);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);

        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.start();

        PulsatorLayout pulsator = findViewById(R.id.pulsator);
        pulsator.start();

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        if (intent.getData() != null){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            final String email = pref.getString("email", null);
            final String emailLink = intent.getData().toString();
            if(firebaseAuth.isSignInWithEmailLink(emailLink)) {

                if (email == null) {
                    return;
                }
                firebaseAuth.signInWithEmailLink(email, emailLink)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    AuthResult result = task.getResult();
                                    FirebaseUser user = result.getUser();
                                    if(user != null){
                                        UserController userController = new UserController(new User(), SplashActivity.this);
                                        userController.setUserId(user.getUid());
                                        userController.setUserFullName(
                                                email.split("\\.")[0].substring(0,1).toUpperCase() + email.split("\\.")[0].substring(1).toLowerCase() + " "+
                                                email.split("\\.")[1].substring(0,1).toUpperCase() + email.split("\\.")[1].replace("@my", "").substring(1).toLowerCase());
                                        userController.setUserType(UserType.STUDENT);
                                        userController.setUserEmail(email);
                                        userController.saveModel();
                                        userController.update();
                                    }
                                } else {
                                    Log.e("", "Error signing in with email link", task.getException());
                                }
                            }
                        });
            }
        } else {
            if (firebaseAuth.getCurrentUser() != null) {
                User user = new User(firebaseAuth.getCurrentUser().getUid());
                new UserController(user, this);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }

    @Override
    public void update(final Model model) {
        if(model instanceof User){
            final Intent intent;
            switch (((User)model).getUserType()){
                case ADMINISTRATOR:
                    intent = new Intent(this, AdminOverview.class);
                    break;
                case DRIVER:
                    intent = new Intent(this, DriverSetup.class);
                    break;
                default:
                case STUDENT:
                    intent = new Intent(this, StudentOverview.class);
                    break;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    intent.putExtra("user", (User) model);
                    startActivity(intent);

                    finish();
                }
            }, 1600);
        }
    }
}
