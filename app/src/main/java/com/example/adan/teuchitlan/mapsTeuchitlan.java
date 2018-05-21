package com.example.adan.teuchitlan;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;




public class mapsTeuchitlan extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private int bandera=-1;
    private boolean banderaArrays=false;
    private int cantidadSnaps=0;
    private static final LatLng coordenadasTeuchitlan = new LatLng(20.685499, -103.847768);

    private DatabaseReference mDatabase, referencia;

     String latitud;
     String longitud;
     FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String>beaconsVisitados=new ArrayList<String>();
    ArrayList<Beacon>beaconsRegistrados=new ArrayList<Beacon>();
    ArrayList<beaconsMaps>beaconColor=new ArrayList<beaconsMaps>();
    ArrayList<beaconsMaps>beaconBN=new ArrayList<beaconsMaps>();


    ArrayList <String[]> ubicacionesBeacons = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mapa_progreso);
        Log.d("entro a maps","teuchitlan");
        mDatabase = FirebaseDatabase.getInstance().getReference();  //se crean referencias a la base de datos
        referencia = mDatabase.child("Teuchitlan/Users/"+user.getUid()+"/BeaconsVisitados");
        beaconsRegistrados=getIntent().getParcelableArrayListExtra("beaconsRegistrados");
        if(new Operaciones().conectadoInternet(this)){    //se verifica que el dispositivo este conectado a internet

            referencia.addListenerForSingleValueEvent(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cantidadSnaps=(int) dataSnapshot.getChildrenCount();
                    bandera=0;
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapChild : dataSnapshot.getChildren()) {

                            beaconsVisitados.add(snapChild.child("idBeacon").getValue().toString());
                            bandera++;
                            construirArrays();
                        }
                    }
                    else{
                        construirArrays();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .build();

    }

    public void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    public void construirArrays(){
        if(bandera>=cantidadSnaps){
            Log.d("construirArrays","beacons visitados"+beaconsVisitados.size()+" beacons reg"+beaconsRegistrados.size());

            for (Beacon item: beaconsRegistrados){

                if(beaconsVisitados.contains(item.id)){

                    String[] parts=item.ubicacion.split(",");
                    beaconColor.add(new beaconsMaps(" ",item.id,parts[0],parts[1]));//crea objeto beacon visitado

                }
                else{

                    String[] parts=item.ubicacion.split(",");
                    beaconBN.add(new beaconsMaps(" ",item.id,parts[0],parts[1]));
                }
            }
            int status= GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());


            if(status== ConnectionResult.SUCCESS){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

            }else{
                Dialog dialog=GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(),10);
                dialog.show();
            }
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json));

        checkLocationandAddToMap();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }


        for (beaconsMaps it: beaconBN) {
            Log.d("for beaconBN","cantidad "+beaconBN.size());
            LatLng coordenada = new LatLng(Double.parseDouble(it.latitud), Double.parseDouble(it.longitud));
            setMarker(coordenada);
        }
        for (beaconsMaps it: beaconColor) {

            LatLng coordenada = new LatLng(Double.parseDouble(it.latitud), Double.parseDouble(it.longitud));
            setMArkerVisited(coordenada,"","");
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadasTeuchitlan, 16.0f));
    }

    @Override
    public void onConnected(Bundle bundle){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    checkLocationandAddToMap();
                } else
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void checkLocationandAddToMap() {
        //Checking if the user has granted the permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requesting the Location permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

    }


    private void setMarker(LatLng posicion) {

        Marker myMarker = mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.beacon_grey)));
    }

    private void setMArkerVisited(LatLng posicion, String Titulo, String info){
        Marker myMarkerVisited = mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .title(Titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.beacon_azul )));
    }

    private void setMArkerCurrentPlace(LatLng posicion, String titulo, String info){

        Marker myMarkerCurrent = mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .title(titulo)
                .snippet(info));
               // .icon(BitmapDescriptorFactory.fromResource(R.drawable.beacon_purple_current_place)));
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




}

