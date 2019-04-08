package edu.uwi.sta.srts.views.admin;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import org.w3c.dom.Text;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.models.utils.UserType;
import edu.uwi.sta.srts.views.InfoDialogHelper;
import edu.uwi.sta.srts.views.LoginActivity;
import edu.uwi.sta.srts.views.fragments.DriversFragment;
import edu.uwi.sta.srts.views.fragments.RoutesFragment;
import edu.uwi.sta.srts.views.fragments.VehiclesFragment;

public class AdminOverview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Routes");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment instanceof DriversFragment){
                    Intent intent = new Intent(AdminOverview.this, EditUser.class);
                    //intent.putExtra("user", getIntent().getSerializableExtra("user"));
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = RoutesFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        final Snackbar offlineSnackbar = Snackbar.make(findViewById(R.id.content_frame),
                "No internet. All changes saved locally.", Snackbar.LENGTH_INDEFINITE);
        offlineSnackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offlineSnackbar.dismiss();
            }
        });

        DatabaseHelper.attachIsOnlineListener(offlineSnackbar);

        View headerLayout = navigationView.getHeaderView(0);

        TextView navName = (TextView) headerLayout.findViewById(R.id.navName);
        TextView navEmail = (TextView) headerLayout.findViewById(R.id.navEmail);
        FloatingActionButton navEditUser = (FloatingActionButton)headerLayout.findViewById(R.id.edit_user);
        navEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOverview.this, EditUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        new UserController(new User(FirebaseAuth.getInstance().getCurrentUser().getUid()), new UserView(navName, navEmail));
    }

    public class UserView implements edu.uwi.sta.srts.views.View {

        TextView name;
        TextView email;

        public UserView(TextView name, TextView email){
            this.name = name;
            this.email = email;
        }

        @Override
        public void update(Model model) {
            if(model instanceof User && name != null && email != null){
                this.name.setText(((User)model).getFullName());
                this.email.setText(((User)model).getEmail());
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_overview, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (id) {
            case R.id.nav_routes:
                fragment = RoutesFragment.newInstance();
                getSupportActionBar().setTitle("Routes");
                break;
            case R.id.nav_drivers:
                fragment = DriversFragment.newInstance();
                getSupportActionBar().setTitle("Drivers");
                break;
            case R.id.nav_vehicles:
                fragment = VehiclesFragment.newInstance();
                getSupportActionBar().setTitle("Vehicles");
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                if(fragment instanceof RoutesFragment) {
                    InfoDialogHelper.showInfoDialog(this,
                            "Routes",
                            "This screen shows you your automatically calculated cumulative " +
                                    "and degree GPAs. It also compares them to your target GPAs " +
                                    "(if any)." + "<br><br>" +
                                    "<b>Yearly GPA Histogram</b>" + "<br>" +
                                    "A histogram of your yearly GPAs will be displayed if you have " +
                                    "GPA data for two or more years. A list of clickable links to " +
                                    "each year will be under the graph." + "<br><br>" +
                                    "<b>Semesterly GPA Histogram</b>" + "<br>" +
                                    "A histogram of your semesterly GPAs will be displayed if you have " +
                                    "GPA data for two or more semesters. A list of clickable links to " +
                                    "each semester will be under the graph." + "<br><br>" +
                                    "<b>Adding information</b>" + "<br>" +
                                    "You can add information for years, semesters, courses, " +
                                    "assignments and exams by clicking the green button on the " +
                                    "bottom right.");
                }
                return true;
            default:
                toggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
