package com.example.adan.teuchitlan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.RegionUtils;
import com.estimote.coresdk.observation.utils.Proximity;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;

public class MainActivity extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener {
        BeaconManager beaconManager;
    private boolean notificationAlreadyShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btnMapaProgreso = (Button)findViewById(R.id.button);
        btnMapaProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mapaProgreso.class);
                startActivityForResult(intent,0);
            }
        });

        Button btnProgreso = (Button)findViewById(R.id.button2);
        btnProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), progreso.class);
                startActivityForResult(intent,0);
            }
        });

        Button btnBeaconActual = (Button)findViewById(R.id.button3);
        btnBeaconActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), beaconActual.class);
                startActivityForResult(intent,0);
            }
        });

        Button btnQueHacer = (Button)findViewById(R.id.button4);
        btnQueHacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), queHacer.class);
                startActivityForResult(intent,0);
            }
        });

        beaconManager = new BeaconManager(getApplicationContext());
        EstimoteSDK.initialize(getApplicationContext(), "<teuchitlan-84i>", "<4f94bfb6df0d2b5ddff8c4d4b66ef73e>");

        beaconManager.setLocationListener(new BeaconManager.LocationListener() {
            @Override
            public void onLocationsFound(List<EstimoteLocation> beacons) {
                Log.d("LocationListener", "Nearby beacons: " + beacons);

                String beaconRosa="[cd28bb55cf08c21ccb05b2bb656a7632]";
                String beaconAzul="[e062c4d5b22baeeacf2718691ac7902c]";
                String beaconMenta="[80455b21003d1c2a550819a5d942c21b]";
                for (EstimoteLocation beacon : beacons) {
                    Log.d("xxxxxx","entro al for de beacon");
                    Log.d("numero de beacon",beacon.id.toString());
                    if (beacon.id.toString().equals(beaconRosa)
                            && RegionUtils.computeProximity(beacon) == Proximity.NEAR) {
                        Log.d("xxxxxx","entro al if de beacon");
                        Bitmap bMap = BitmapFactory.decodeFile("/home/adan/Descargas/Teuchitlan/app/src/main/res/drawable/beaconyellow.png");
                        showNotification("Descubriste beacon ", "aprende de el.",bMap,"rosa");
                    }
                    else if(beacon.id.toString().equals(beaconAzul)
                            && RegionUtils.computeProximity(beacon) == Proximity.NEAR){
                        Bitmap bMap = BitmapFactory.decodeFile("/home/adan/Descargas/Teuchitlan/app/src/main/res/drawable/beaconblue.png");
                        showNotification("Descubriste beacon ", "aprende de el.",bMap,"azul");
                    }

                    else if(beacon.id.toString().equals(beaconMenta)
                            && RegionUtils.computeProximity(beacon) == Proximity.NEAR){
                        Bitmap bMap = BitmapFactory.decodeFile("/home/adan/Descargas/Teuchitlan/app/src/main/res/drawable/beaconblue.png");
                        showNotification("Descubriste beacon ", "aprende de el.",bMap,"menta");
                    }

                }
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override public void onServiceReady() {
                beaconManager.startLocationDiscovery();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("tag","entro a onResume");
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(getApplicationContext(), mapaProgreso.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), progreso.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), beaconActual.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), queHacer.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showNotification(String title, String message, Bitmap bMap,String color) {

        if (notificationAlreadyShown) { return; }

        Intent notifyIntent = new Intent(this, InformacionBeacon.class);
        notifyIntent.putExtra("beacon",color);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("xxxxx","entro a showNoti");
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bMap)
                .setContentTitle(title + color)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
        notificationAlreadyShown = true;
    }

}
