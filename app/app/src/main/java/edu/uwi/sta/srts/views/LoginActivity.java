package edu.uwi.sta.srts.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.utils.Closable;
import edu.uwi.sta.srts.utils.Utils;

public class LoginActivity extends AppCompatActivity implements Closable {

    private android.view.View loading;
    private boolean passwordVisible = false;
    private boolean studentMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final TextView emailError = findViewById(R.id.emailError);
        final TextView passwordError = findViewById(R.id.passwordError);
        final ImageButton passwordToggle = findViewById(R.id.passwordToggle);
        loading = findViewById(R.id.progressBar);


        Utils.setUpActivations(this, emailEditText, findViewById(R.id.emailUnderline));
        Utils.setUpActivations(this, passwordEditText, findViewById(R.id.passwordUnderline));

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                if(studentMode && !s.toString().endsWith("@my.uwi.edu")){
                    email += "@my.uwi.edu";
                }

                if(Utils.isValidEmail(email) ){
                    findViewById(R.id.emailDone).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.emailDone).setVisibility(View.GONE);
                }

                if(emailError.getVisibility() == View.VISIBLE){
                    emailError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() >= 8){
                    findViewById(R.id.passwordDone).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.passwordDone).setVisibility(View.GONE);
                }

                if(passwordError.getVisibility() == View.VISIBLE){
                    passwordError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordVisible){
                    passwordVisible = false;
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.round_visibility_24);
                }else{
                    passwordVisible = true;
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.round_visibility_off_24);
                }
            }
        });

        final TextView login = findViewById(R.id.login);
        login.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                String emailText = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(studentMode && !emailText.endsWith("@my.uwi.edu")){
                    emailText += "@my.uwi.edu";
                }

                final String email = emailText;

                boolean error = false;

                if (emailError.getVisibility() == View.VISIBLE) {
                    emailError.setText("Please enter a valid email");
                    emailError.setVisibility(View.VISIBLE);
                    findViewById(R.id.emailUnderline).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    error = true;
                } else {
                    emailError.setVisibility(View.INVISIBLE);
                }

                if (!studentMode && passwordError.getVisibility() == View.VISIBLE) {
                    passwordError.setText("Password must be at least 8 characters long");
                    passwordError.setVisibility(View.VISIBLE);
                    findViewById(R.id.passwordUnderline).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    error = true;
                } else {
                    passwordError.setVisibility(View.INVISIBLE);
                }

                if (error) {
                    return;
                }

                loading.setVisibility(android.view.View.VISIBLE);

                if (studentMode) {
                    ActionCodeSettings actionCodeSettings =
                            ActionCodeSettings.newBuilder()
                                    .setUrl("https://play.google.com/store/apps/details?id=edu.uwi.sta.srts")
                                    .setHandleCodeInApp(true)
                                    .setAndroidPackageName(
                                            "edu.uwi.sta.srts",
                                            true,
                                            "21")

                                    .build();
                    mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loading.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Snackbar.make(findViewById(R.id.rl), "Confirmation email sent to " + email + "", Snackbar.LENGTH_INDEFINITE).show();
                                    }else{
                                        Log.d("meh", email + "  " + task.getException().toString());
                                    }
                                }
                            });

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", email);
                    editor.apply();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            DatabaseHelper.getInstance().getDatabaseReference("users").child(currentUser.getUid())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            User user = dataSnapshot.getValue(User.class);
                                                            if (user != null) {
                                                                close(user);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }
                                    } else {
                                        Snackbar.make(findViewById(R.id.rl), "Incorrect email or password. Try again", Snackbar.LENGTH_SHORT).show();
                                        loading.setVisibility(android.view.View.GONE);
                                    }

                                }
                            });
                }
            }
        });


        final TextView switchMode = findViewById(R.id.switchMode);
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentMode = !studentMode;

                View passwordLayout = findViewById(R.id.passwordLayout);
                if(studentMode){
                    passwordLayout.setVisibility(View.GONE);
                    login.setText("Continue");
                    String styledText = "Are you a driver or administrator? <b><font color='#009688'>Click here</font></b>.";
                    switchMode.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                    emailEditText.setHint("Enter your UWI email");
                    findViewById(R.id.uwiEmailPlaceholder).setVisibility(View.VISIBLE);
                }else{
                    passwordLayout.setVisibility(View.VISIBLE);
                    login.setText("Sign In");
                    String styledText = "Are you a student? <b><font color='#009688'>Click here</font></b>.";
                    switchMode.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                    emailEditText.setHint("Enter email");
                    findViewById(R.id.uwiEmailPlaceholder).setVisibility(View.GONE);
                }
            }
        });

        switchMode.performClick();
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
    public void close(User user) {
        loading.setVisibility(android.view.View.GONE);
        Intent intent;
        switch (user.getUserType()){
            default:
            case STUDENT:
                intent = new Intent(this, StudentOverview.class);break;
            case DRIVER:
                intent = new Intent(this, DriverOverview.class);break;
            case ADMINISTRATOR:
                intent = new Intent(this, AdminOverview.class);break;
        }
        intent.putExtra("user", user);
        startActivity(intent);
        this.finish();
    }
}
