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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

    private static final LatLng coordenadasTeuchitlan = new LatLng(20.685499, -103.847768);
    private static final LatLng plazaTeuchitlan = new LatLng(20.683778, -103.847250);
    private static final LatLng piramideMenorGuachimontones = new LatLng(20.694792, -103.836523);
    private static final LatLng rioTeuchitlan = new LatLng(20.686091, -103.843438);
    private static final LatLng casaCultura = new LatLng(20.683610, -103.848356);

    private Marker mPlazaTeuchitlan;

    private DatabaseReference mDatabase, referencia;


     String ubicacion;
     String latitud;
     String longitud;
     String [] partes;
     String estado;

     int counterBeacons = 0;
     //beaconsData beacons[] = new beaconsData[];

    ArrayList <String[]> ubicacionesBeacons = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        referencia = mDatabase.child("Teuchitlan/beacons");

        if(conectadoInternet()){
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapChild : dataSnapshot.getChildren()){

                        String[] arreglo=new String[2];

                        arreglo[0] = snapChild.child("ubicacion").getValue().toString();
                        arreglo[1]=  snapChild.child("referencia").getValue().toString();
                        ubicacionesBeacons.add(arreglo);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mapa_progreso);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .build();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

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

    public void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkLocationandAddToMap();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        for (String[] object: ubicacionesBeacons) {
            String [] partes = object[0].split(",");
            latitud = partes[0];
            longitud = partes[1];
            Log.d(latitud, longitud);

            double lat = Double.parseDouble(latitud);
            double lon = Double.parseDouble(longitud);

            LatLng coordenada = new LatLng(lat, lon);


            if (object[1].equals("visitado")) {
                setMArkerVisited(coordenada," "," ");
            }
            else if(object[1].equals("no visitado")){
                setMarker(coordenada);
            }

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

        //Fetching the last known location using the Fus
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
     //   MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are Here");

        //Adding the created the marker on the map
        //mMap.addMarker(markerOptions);

    }


    private void setMarker(LatLng posicion) {

        Marker myMarker = mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.beaconblue_resized)));
    }

    private void setMArkerVisited(LatLng posicion, String Titulo, String info){
        Marker myMarkerVisited = mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .title(Titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.beaconblue_resized_visited)));
    }

    private void setMArkerCurrentPlace(LatLng posicion, String titulo, String info){

        Marker myMarkerCurrent = mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.beacon_purple_current_place)));
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public boolean conectadoInternet() {

        ConnectivityManager cm;
        NetworkInfo ni;
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        boolean conexion = false;

        if (ni != null) {
            ConnectivityManager connManager1 = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            ConnectivityManager connManager2 = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobile = connManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


            if (mWifi.isConnected() || mMobile.isConnected()) {
                conexion = true;
            }
        } else {
            conexion = false;
        }
        return conexion;
    }


}

class beaconsData{

    private String estado;
    private String coordenadas;

    public String getEstados(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public String getCoordenadas(){
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas){
        this.coordenadas = coordenadas;
    }
}
