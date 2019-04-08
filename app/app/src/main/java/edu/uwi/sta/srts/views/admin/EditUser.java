package edu.uwi.sta.srts.views.admin;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.utils.UserType;
import edu.uwi.sta.srts.views.View;

public class EditUser extends AppCompatActivity implements View {

    private boolean isEditMode = true;

    private UserController userController;

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout fullNameTextInputLayout;
    private TextView userTypeText;
    private Button resetPasswordButton;

    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        User user = (User) getIntent().getSerializableExtra("user");

        if(user == null){
            user = new User();
            isEditMode = false;
        }
        userController = new UserController(user, this);

        Button signUp = (Button) findViewById(R.id.signUp);

        emailTextInputLayout = (TextInputLayout)findViewById(R.id.emailLayout);
        passwordTextInputLayout = (TextInputLayout)findViewById(R.id.passwordLayout);
        fullNameTextInputLayout = (TextInputLayout)findViewById(R.id.fullNameLayout);
        userTypeText = (TextView)findViewById(R.id.userType);
        resetPasswordButton = (Button)findViewById(R.id.reset);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        if(isEditMode){
            update(user);
            getSupportActionBar().setTitle("Edit User");
        }else{
            userController.setUserType(UserType.DRIVER);
            getSupportActionBar().setTitle("Add Driver");
        }

        emailTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = userController.setUserEmail(String.valueOf(s));
                if(error != null){
                    emailTextInputLayout.setError(error);
                }else{
                    emailTextInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fullNameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = userController.setUserFullName(String.valueOf(s));
                if(error != null){
                    fullNameTextInputLayout.setError(error);
                }else{
                    fullNameTextInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUp.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                userController.saveModel();

                if(!isEditMode){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            userController.getUserEmail(), passwordTextInputLayout.getEditText().toString().trim());
                }

                finish();
            }
        });

        userTypeText.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(EditUser.this, userTypeText);
                menu.getMenuInflater()
                        .inflate(R.menu.default_menu, menu.getMenu());

                menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "Driver");
                menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "Administrator");

                menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == 0){
                            userController.setUserType(UserType.DRIVER);
                            userTypeText.setText("Driver");
                            return true;
                        }else if(menuItem.getItemId() == 1){
                            userController.setUserType(UserType.ADMINISTRATOR);
                            userTypeText.setText("Administrator");
                            return true;
                        }
                        return false;
                    }
                });
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
        if(model instanceof User){
            emailTextInputLayout.getEditText().setText(((User)model).getEmail());
            fullNameTextInputLayout.getEditText().setText(((User)model).getFullName());
            passwordTextInputLayout.setVisibility(android.view.View.GONE);

            emailTextInputLayout.setEnabled(false);
            resetPasswordButton.setVisibility(android.view.View.VISIBLE);

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
