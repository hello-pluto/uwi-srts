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

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.utils.UserType;
import edu.uwi.sta.srts.utils.Utils;

public class EditUser extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private UserController userController;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText fullNameEditText;
    private TextView userTypeText;

    private TextView emailError;
    private TextView passwordError;
    private TextView fullNameError;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        User user = (User) getIntent().getSerializableExtra("user");

        if(user == null){
            user = new User();
            isEditMode = false;
        }
        userController = new UserController(user, this);

        TextView done = findViewById(R.id.done);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        fullNameEditText = findViewById(R.id.fullname);
        userTypeText = findViewById(R.id.userType);

        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        fullNameError = findViewById(R.id.fullnameError);

        final ImageButton passwordToggle = findViewById(R.id.passwordToggle);

        Utils.setUpActivations(this, emailEditText, findViewById(R.id.emailUnderline));
        Utils.setUpActivations(this, passwordEditText, findViewById(R.id.passwordUnderline));
        Utils.setUpActivations(this, fullNameEditText, findViewById(R.id.fullnameUnderline));

        TextView toolbarText = (TextView)findViewById(R.id.toolbarText);

        if(isEditMode){
            update(user);
            toolbarText.setText("Edit User");
        }else{
            userController.setUserType(UserType.DRIVER);
            toolbarText.setText("Add User");
        }

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = userController.setUserEmail(String.valueOf(s));
                if(error == null){
                    findViewById(R.id.emailDone).setVisibility(android.view.View.VISIBLE);
                }else{
                    findViewById(R.id.emailDone).setVisibility(android.view.View.GONE);
                    emailError.setText(error);
                }

                if(emailError.getVisibility() == android.view.View.VISIBLE){
                    emailError.setVisibility(android.view.View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() >= 8){
                    findViewById(R.id.passwordDone).setVisibility(android.view.View.VISIBLE);
                }else{
                    findViewById(R.id.passwordDone).setVisibility(android.view.View.GONE);
                    passwordError.setText("Password must be 8 characters long");
                }

                if(passwordError.getVisibility() == android.view.View.VISIBLE){
                    passwordError.setVisibility(android.view.View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordToggle.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
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

        fullNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = userController.setUserFullName(String.valueOf(s));
                if(error == null){
                    findViewById(R.id.fullnameDone).setVisibility(android.view.View.VISIBLE);
                }else{
                    findViewById(R.id.fullnameDone).setVisibility(android.view.View.GONE);
                    fullNameError.setText(error);
                }

                if(fullNameError.getVisibility() == android.view.View.VISIBLE){
                    fullNameError
                            .setVisibility(android.view.View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                boolean error = false;

                if(findViewById(R.id.fullnameDone).getVisibility() != android.view.View.VISIBLE){
                    fullNameError.setVisibility(android.view.View.VISIBLE);
                    error = true;
                }

                if(isEditMode){
                    if(error){
                        return;
                    }
                    userController.saveModel();
                    finish();
                }else{
                    if(findViewById(R.id.emailDone).getVisibility() != android.view.View.VISIBLE){
                        emailError.setVisibility(android.view.View.VISIBLE);
                        error = true;
                    }
                    if(findViewById(R.id.passwordDone).getVisibility() != android.view.View.VISIBLE){
                        passwordError.setVisibility(android.view.View.VISIBLE);
                        error = true;
                    }

                    if(error){
                        return;
                    }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            userController.getUserEmail(), passwordEditText.getText().toString().trim())
                            .addOnCompleteListener(EditUser.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        try {
                                            FirebaseUser user = task.getResult().getUser();
                                            userController.setUserId(user.getUid());
                                            userController.saveModel();
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

        if(!isEditMode) {
            findViewById(R.id.userTypeLayout).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(EditUser.this, findViewById(R.id.userTypeLayout));
                    menu.getMenuInflater()
                            .inflate(R.menu.default_menu, menu.getMenu());


                    menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "Administrator");
                    menu.getMenu().add(R.id.add_group, 1, Menu.NONE, "Driver");

                    menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == 0) {
                                userController.setUserType(UserType.ADMINISTRATOR);
                                userTypeText.setText("Administrator");
                                return true;
                            } else if (menuItem.getItemId() == 1) {
                                userController.setUserType(UserType.DRIVER);
                                userTypeText.setText("Driver");
                                return true;
                            }
                            return false;
                        }
                    });

                    menu.show();
                }
            });
        }else{
            userTypeText.setTextColor(Color.parseColor("#bbbbbb"));
            findViewById(R.id.userTypeDropdown).setVisibility(android.view.View.GONE);
        }
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
        if(model instanceof User){
            emailEditText.setText(((User)model).getEmail());
            fullNameEditText.setText(((User)model).getFullName());
            findViewById(R.id.emailDone).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.fullnameDone).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.userTypeDone).setVisibility(android.view.View.VISIBLE);

            findViewById(R.id.passwordLayout).setVisibility(android.view.View.GONE);

            emailEditText.setEnabled(false);

            switch (((User)model).getUserType()){
                case ADMINISTRATOR:
                    userTypeText.setText("Administrator");
                    break;
                case DRIVER:
                    userTypeText.setText("Driver");
                    break;
                    default:
                case STUDENT:
                    userTypeText.setText("Student");
                    break;
            }

        }
    }
}
