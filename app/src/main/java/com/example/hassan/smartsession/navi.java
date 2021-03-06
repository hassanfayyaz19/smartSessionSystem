package com.example.hassan.smartsession;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
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

import com.example.hassan.smartsession.activities.ViewAttendence;
import com.example.hassan.smartsession.activities.changePasswordActivity;
import com.example.hassan.smartsession.activities.loginAcivity;
import com.example.hassan.smartsession.activities.profileActivity;
import com.example.hassan.smartsession.activities.scanActivity;
import com.example.hassan.smartsession.sharedPeference.SharePref;

public class navi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static SharePref prefConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefConfig = new SharePref(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView username = (TextView) headerView.findViewById(R.id.usernameView);
        TextView roll = headerView.findViewById(R.id.rollnoView);
        String userg = prefConfig.readName();
        String rol = prefConfig.readRollNo();
        username.setText(userg);
        roll.setText(rol);

        android.support.v7.widget.GridLayout mainGrid = (android.support.v7.widget.GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logoutUser();
            return true;
        } else if (id == R.id.action_changePass) {
            startActivity(new Intent(this, changePasswordActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(this, scanActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, profileActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(this, changePasswordActivity.class));
        } else if (id == R.id.nav_send) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setSingleEvent(android.support.v7.widget.GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        Intent intent = new Intent(navi.this, scanActivity.class);
                        startActivity(intent);

                    } else if (finalI == 1) {
                        Intent intent = new Intent(navi.this, ViewAttendence.class);
                        startActivity(intent);

                    } else if (finalI == 2) {
                        startActivity(new Intent(navi.this, profileActivity.class));
                    } else if (finalI == 3) {
                        logoutUser();
                    }


                }
            });
        }
    }

    public void logoutUser() {
        prefConfig.writeLoginStatus(false);
        prefConfig.WriteRollNo("user");
        prefConfig.WriteSemester("sems");
        prefConfig.WriteName("name");
        prefConfig.WriteDepartment("depart");
        prefConfig.WriteMacAddress("mac");
        prefConfig.writePassword("password");
        startActivity(new Intent(this, loginAcivity.class));
        finish();
    }
}
