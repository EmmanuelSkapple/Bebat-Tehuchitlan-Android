package com.example.adan.teuchitlan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class beaconActual extends AppCompatActivity {

    private DatabaseReference mDatabase, referencia;
    ArrayList<String> valoresIds=new ArrayList<String>();
    BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_beacon_actual);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        referencia= mDatabase.child("Teuchitlan/beacons");
        if(conectadoInternet()) {
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        Log.d("xxxxxxxxxxxxxxxxxxxx", snapChild.child("id").getValue().toString());

                        valoresIds.add(snapChild.child("id").getValue().toString());
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        beaconManager = new BeaconManager(getApplicationContext());
        EstimoteSDK.initialize(getApplicationContext(), "<teuchitlan-84i>", "<4f94bfb6df0d2b5ddff8c4d4b66ef73e>");

        beaconManager.setLocationListener(new BeaconManager.LocationListener() {
            @Override
            public void onLocationsFound(List<EstimoteLocation> beacons) {

                for(EstimoteLocation beacon : beacons) {
                    int indiceContenedor=valoresIds.indexOf(beacon.id.toString());
                    Log.d(" el id de beacon es : ",Integer.toString(indiceContenedor));
                    if(indiceContenedor!=-1){
                        obtenerLugarHistorico(valoresIds.get(indiceContenedor));
                    }

                }
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override public void onServiceReady() {
                beaconManager.startLocationDiscovery();
            }
        });
    }
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }

    public void obtenerLugarHistorico(String idBeacon) {
        Log.d("tag", "entro a obtener lugar" + idBeacon);
        if (conectadoInternet()) {
            final String idB = idBeacon;
            referencia = mDatabase.child("Teuchitlan/SitiosHistoricos");
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("xxx", "entro a onData fuera del for");
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        Log.d("tag", "entro a obtener lugar en onDataChange" + snapChild.child("idBeacon").getValue().toString());
                        if (idB == snapChild.child("idBeacon").getValue().toString()) ;

                        sitioHistorico sh = new sitioHistorico(snapChild.child("datoCultural").getValue().toString(), snapChild.child("datoCurioso").getValue().toString(),
                                snapChild.child("datoHistorico").getValue().toString(), snapChild.child("datoInteres").getValue().toString(), snapChild.child("id").getValue().toString(),
                                snapChild.child("idBeacon").getValue().toString(), snapChild.child("imagenes").getValue().toString(),
                                snapChild.child("key").getValue().toString(), snapChild.child("nombre").getValue().toString());
                        Log.d("zzzzzzzz", sh.toString());
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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