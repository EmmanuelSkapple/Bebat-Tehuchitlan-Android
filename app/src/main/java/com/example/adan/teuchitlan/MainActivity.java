package com.example.adan.teuchitlan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.RegionUtils;
import com.estimote.coresdk.observation.utils.Proximity;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private boolean notificationAlreadyShown = false;
    String usuario;
    Operaciones op=new Operaciones();
    ArrayList<sitioHistorico> listaSitiosHistoricos=new ArrayList<sitioHistorico>();
    ArrayList<Lugar> listLugares=new ArrayList<Lugar>();
    ArrayList<Beacon>listaBeacons=new ArrayList<Beacon>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        Button btnMapaProgreso = (Button)findViewById(R.id.button);
        ImageView fotoPerfil=(ImageView)findViewById(R.id.circle_image);
        Intent i=this.getIntent();
        listaBeacons=getIntent().getParcelableArrayListExtra("beacons");
        listaSitiosHistoricos=getIntent().getParcelableArrayListExtra("sitios");
        listLugares=getIntent().getParcelableArrayListExtra("lugares");
        Log.d("entro a main","activity");
        Log.d("lista beacons",listaBeacons.get(1).ubicacion.toString());
        Log.d("lista lugares","size() "+listLugares.size());
        Log.d("lista sitios",listaSitiosHistoricos.get(1).nombre.toString());

        Intent iService=new Intent(getApplicationContext(),beaconService.class);
        iService.putParcelableArrayListExtra("listaSitios",listaSitiosHistoricos);
        startService(iService);
        btnMapaProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mapsTeuchitlan.class);
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
                Intent intent = new Intent(getApplicationContext(), QueHacerTabMainActivity.class);
                intent.putParcelableArrayListExtra("lugares",listLugares);
                startActivityForResult(intent,0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("tag","entro a onResume");
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
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
            Intent intent = new Intent(getApplicationContext(), QueHacerTabMainActivity.class);
            intent.putParcelableArrayListExtra("lugares",listLugares);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
