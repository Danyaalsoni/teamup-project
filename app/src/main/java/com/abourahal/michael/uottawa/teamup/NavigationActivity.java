package com.abourahal.michael.uottawa.teamup;

import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,new FirstFragment()).commit();


        sharedPreferences=getSharedPreferences("profilePreference",Context.MODE_PRIVATE);
        if(sharedPreferences.getString("accountKey","n")=="n") {
            try {
                Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                        new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
                startActivityForResult(intent, 1);
            } catch (ActivityNotFoundException e) {

            }
        }
        View header=navigationView.getHeaderView(0);
        ImageView contactImage=(ImageView)header.findViewById(R.id.contactImage);
        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),profileActivity.class);
                startActivity(intent);
            }
        });
        TextView contactName=(TextView) header.findViewById(R.id.contactName);
        contactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),profileActivity.class);
                startActivity(intent);
            }
        });
        TextView contactEmail=(TextView) header.findViewById(R.id.contactEmail);
        contactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),profileActivity.class);
                startActivity(intent);
            }
        });
        contactName.setText(sharedPreferences.getString("nameKey","TeamUp"));
        contactEmail.setText(sharedPreferences.getString("emailKey","TeamUP2TeamUp.com"));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String accountEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            sharedPreferences = getSharedPreferences("profilePreference", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.putString("emailKey",accountEmail);
            editor.putString("accountKey","y");
            editor.commit();
        }
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
        getMenuInflater().inflate(R.menu.navigation, menu);
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
            return true;
        }else if(id==R.id.action_help){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Help");
                dialog.setMessage(Html.fromHtml("<b>How to add an event? </b>"+"<br/>" + "1) Tap on a location on the map <br/>" + "2) Click the Add button on the bottom right of the screen <br/>" + "3) Enter all the required information in the next screen <br/>" + "4) Click the Add button again <br/>"
                                    +"<br/><b>How to join an Event?</b> <br/>"+"1) Click on an Event on the Map <br/> 2) In the dialog that pops up above the event click the join button <br/><br/>"
                                    +"<b>How to Leave an Event?</b> <br/>"+"1) Click on an Event on the Map <br/> 2) In the dialog that pops up above the event click the leave button"));
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setCancelable(false);
                dialog.create().show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_first_layout) {
            getSupportActionBar().setTitle("Home");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new FirstFragment()).commit();

        } else if (id == R.id.nav_second_layout) {
            getSupportActionBar().setTitle("Events");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new SecondFragment()).commit();

        } else if (id == R.id.nav_about) {
            getSupportActionBar().setTitle("About the Product");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ThirdFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
