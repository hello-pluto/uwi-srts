package edu.uwi.sta.srts.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.utils.UserType;

public class SplashActivity extends AppCompatActivity implements View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
                                        userController.setUserFullName(email.replace("@my.uwi.edu",""));
                                        userController.setUserVerified(true);
                                        userController.setUserType(UserType.STUDENT);
                                        userController.setUserEmail(email);
                                        userController.saveModel();
                                        userController.updateView();
                                    }
                                } else {
                                    Log.e("", "Error signing in with email link", task.getException());
                                }
                            }
                        });
            }else {

                AuthCredential credential =
                        EmailAuthProvider.getCredentialWithLink(email, emailLink);

                firebaseAuth.getCurrentUser().reauthenticateAndRetrieveData(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    AuthResult result = task.getResult();
                                    FirebaseUser user = result.getUser();
                                    if(user != null){
                                        UserController userController = new UserController(new User(), SplashActivity.this);
                                        userController.setUserId(user.getUid());
                                        userController.setUserVerified(true);
                                        userController.setUserType(UserType.STUDENT);
                                        userController.setUserEmail(email);
                                        userController.saveModel();
                                        userController.updateView();
                                    }
                                } else {
                                    Log.e("", "Error reauthenticating", task.getException());
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
    public void update(Model model) {
        if(model instanceof User){
            Intent intent;
            switch (((User)model).getUserType()){
                case ADMINISTRATOR:
                    intent = new Intent(this, AdminOverview.class);
                    break;
                case DRIVER:
                    intent = new Intent(this, DriverOverview.class);
                    break;
                default:
                case STUDENT:
                    intent = new Intent(this, StudentOverview.class);
                    break;
            }
            intent.putExtra("user", (User)model);
            startActivity(intent);

            finish();
        }
    }
}
