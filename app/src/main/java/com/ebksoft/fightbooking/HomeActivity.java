package com.ebksoft.fightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView txtTitle;
    private Button btOneWay, btRoundTrip;

    private View rlTimeToParent;

    private int countAdult = 1, countChild = 1, countIndent = 1;
    private Button btDownAdult, btUpAdult, btDownChild, btUpChild, btDownIndent, btUpIndent;
    private TextView tvAdult, tvChild, tvIndent;

    private boolean isNormalType = true;
    private Button btNext, btPrev;
    private TextView tvTicketType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadView();

        loadData();
    }

    private void loadView() {
        initNav();

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        findViewById(R.id.imgMenu).setOnClickListener(this);

        findViewById(R.id.rlPlaceFrom).setOnClickListener(this);
        findViewById(R.id.imgSearchFight).setOnClickListener(this);
        findViewById(R.id.rlTimeGo).setOnClickListener(this);
        findViewById(R.id.rlTimeTo).setOnClickListener(this);

        btOneWay = (Button) findViewById(R.id.btOneWay);
        btRoundTrip = (Button) findViewById(R.id.btRoundTrip);
        rlTimeToParent = findViewById(R.id.rlTimeToParent);

        btOneWay.setOnClickListener(this);
        btRoundTrip.setOnClickListener(this);

        btDownAdult = (Button) findViewById(R.id.btDownAdult);
        btUpAdult = (Button) findViewById(R.id.btUpAdult);
        btDownChild = (Button) findViewById(R.id.btDownChild);
        btUpChild = (Button) findViewById(R.id.btUpChild);
        btDownIndent = (Button) findViewById(R.id.btDownIndent);
        btUpIndent = (Button) findViewById(R.id.btUpIndent);

        tvAdult = (TextView) findViewById(R.id.tvAdult);
        tvChild = (TextView) findViewById(R.id.tvChild);
        tvIndent = (TextView) findViewById(R.id.tvIndent);

        btDownAdult.setOnClickListener(this);
        btUpAdult.setOnClickListener(this);
        btDownChild.setOnClickListener(this);
        btUpChild.setOnClickListener(this);
        btDownIndent.setOnClickListener(this);
        btUpIndent.setOnClickListener(this);


        btNext = (Button) findViewById(R.id.btNext);
        btPrev = (Button) findViewById(R.id.btPrev);
        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);

        tvTicketType = (TextView) findViewById(R.id.tvTicketType);
    }

    private void loadData() {
        txtTitle.setText(getString(R.string.search_fight).toUpperCase(Locale.getDefault()));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imgMenu:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;

            case R.id.rlPlaceFrom:
                startActivity(new Intent(this, ChooseAirportActivity.class));
                break;

            case R.id.imgSearchFight:
                startActivity(new Intent(this, SearchFightResultActivity.class));
                break;

            case R.id.rlTimeGo:
                Intent i = new Intent(this, ChooseTimeActivity.class);
                i.putExtra("isRoundTrip", false);
                startActivity(i);
                break;

            case R.id.rlTimeTo:
                Intent i2 = new Intent(this, ChooseTimeActivity.class);
                i2.putExtra("isRoundTrip", true);
                startActivity(i2);
                break;

            case R.id.btOneWay:
                rlTimeToParent.setVisibility(View.INVISIBLE);
                btOneWay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                btRoundTrip.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;

            case R.id.btRoundTrip:
                rlTimeToParent.setVisibility(View.VISIBLE);
                btOneWay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                btRoundTrip.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                break;

            case R.id.btDownAdult:
                if (countAdult != 0)
                    countAdult--;
                updateNumPassenger();
                break;

            case R.id.btUpAdult:
                countAdult++;
                updateNumPassenger();
                break;

            case R.id.btDownChild:
                if (countChild != 0)
                    countChild--;
                updateNumPassenger();
                break;

            case R.id.btUpChild:
                countChild++;
                updateNumPassenger();
                break;

            case R.id.btDownIndent:
                if (countIndent != 0)
                    countIndent--;
                updateNumPassenger();
                break;

            case R.id.btUpIndent:
                countIndent++;
                updateNumPassenger();
                break;

            case R.id.btPrev:
            case R.id.btNext:

                isNormalType = !isNormalType;
                updateTicketType();
                break;
        }
    }

    private void updateNumPassenger() {
        tvAdult.setText(String.valueOf(countAdult));
        tvChild.setText(String.valueOf(countChild));
        tvIndent.setText(String.valueOf(countIndent));
    }

    private void updateTicketType() {
        if (isNormalType) {
            tvTicketType.setText(getString(R.string.ticket_type_normal));
        } else {
            tvTicketType.setText(getString(R.string.ticket_type_vip));
        }
    }

    private void initNav() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        getMenuInflater().inflate(R.menu.home, menu);
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
