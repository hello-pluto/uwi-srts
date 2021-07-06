package edu.uwi.sta.srts.views;

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

import com.google.firebase.auth.FirebaseAuth;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.views.fragments.AlertsFragment;
import edu.uwi.sta.srts.views.fragments.DriversFragment;
import edu.uwi.sta.srts.views.fragments.RoutesFragment;
import edu.uwi.sta.srts.views.fragments.StopsFragment;
import edu.uwi.sta.srts.views.fragments.ShuttlesFragment;

public class AdminOverview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;

    private Fragment fragment;

    private TextView toolbarText;

    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarText = (TextView)findViewById(R.id.toolbarText);
        toolbarText.setText("Routes");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(fragment instanceof DriversFragment){
                    intent = new Intent(AdminOverview.this, EditUser.class);
                }else if(fragment instanceof RoutesFragment){
                    intent = new Intent(AdminOverview.this, EditRoute.class);
                }else if(fragment instanceof ShuttlesFragment){
                    intent = new Intent(AdminOverview.this, EditShuttle.class);
                }else if(fragment instanceof StopsFragment){
                    intent = new Intent(AdminOverview.this, EditStop.class);
                }else if(fragment instanceof AlertsFragment) {
                    intent = new Intent(AdminOverview.this, EditAlert.class);
                }

                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = RoutesFragment.newInstance(true);

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
        TextView navType = (TextView)headerLayout.findViewById(R.id.navType);
        FloatingActionButton navEditUser = (FloatingActionButton)headerLayout.findViewById(R.id.edit_user);
        navEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOverview.this, EditUser.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        userController = new UserController(new User(FirebaseAuth.getInstance().getCurrentUser().getUid()), new UserView(navName, navType, navEmail));
    }

    public class UserView implements edu.uwi.sta.srts.views.View {

        TextView name;
        TextView type;
        TextView email;

        public UserView(TextView name, TextView type, TextView email){
            this.name = name;
            this.type = type;
            this.email = email;
        }

        @Override
        public void update(Model model) {
            if(model instanceof User && name != null && email != null){
                this.name.setText(((User)model).getFullName());
                this.email.setText(((User)model).getEmail());
                this.type.setText("Administrator");
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
                fragment = RoutesFragment.newInstance(true);
                toolbarText.setText("Routes");
                break;
            case R.id.nav_drivers:
                fragment = DriversFragment.newInstance();
                toolbarText.setText("Drivers");
                break;
            case R.id.nav_shuttles:
                fragment = ShuttlesFragment.newInstance();
                ((ShuttlesFragment)fragment).user = (User)userController.getModel();
                toolbarText.setText("Shuttles");
                break;
            case R.id.nav_stops:
                fragment = StopsFragment.newInstance();
                toolbarText.setText("Stops");
                break;
            case R.id.nav_alerts:
                fragment = AlertsFragment.newInstance(true);
                toolbarText.setText("Alerts");
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
                            "");
                }
                return true;
            default:
                toggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
