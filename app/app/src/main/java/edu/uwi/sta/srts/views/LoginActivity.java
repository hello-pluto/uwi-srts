package edu.uwi.sta.srts.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setTitle("");

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final TextInputLayout emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        final TextInputLayout passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        loading = findViewById(R.id.progressBar);
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                String email = emailTextInputLayout.getEditText().getText().toString().trim();
                String password = passwordTextInputLayout.getEditText().getText().toString().trim();

                if(!Utils.isValidEmail(email)){
                    emailTextInputLayout.setError("Please enter a valid email");
                    return;
                }else{
                    emailTextInputLayout.setError(null);
                }

                if(password.length() < 8){
                    passwordTextInputLayout.setError("Password must be at least 8 characters long");
                    return;
                }else{
                    passwordTextInputLayout.setError(null);
                }

                loading.setVisibility(android.view.View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    DatabaseHelper.getInstance().getDatabaseReference("users").child(currentUser.getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    User user = dataSnapshot.getValue(User.class);
                                                    //FirebaseDatabaseHelper.load(user, LoginActivity.this);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                } else {
                                    Snackbar.make(findViewById(R.id.rl), "Incorrect email or password. Try again", Snackbar.LENGTH_SHORT).show();
                                    loading.setVisibility(android.view.View.GONE);
                                }

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
    public void close(User user) {
        loading.setVisibility(android.view.View.GONE);
        Intent intent;
        switch (user.getUserType()){
            default:
            case STUDENT:
                intent = new Intent(this, ViewVehiclesList.class);break;
        }
        intent.putExtra("user", user);
        startActivity(intent);
        this.finish();
    }
}
