package com.example.adan.teuchitlan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class beaconActual extends AppCompatActivity {


    ArrayList<String> valoresIds=new ArrayList<String>();
    ArrayList<Beacon> listaBeacons=new ArrayList<Beacon>();
    ArrayList<sitioHistorico>listaSitios=new ArrayList<sitioHistorico>();
    ArrayList<String>fotos=new ArrayList<String>();
    ArrayList<String>beaconsVisitados=new ArrayList<String>();
    sitioHistorico sitioBeacon;
    BeaconManager beaconManager;
    Dialog myDialog;

    String imagenes;
    boolean ui=true;
    int ban=-1;
    int cantidadSnaps=0;
    int service;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase, referencia,referencia2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_beacon_actual);
        Intent i=getIntent();
        myDialog = new Dialog(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listaSitios=getIntent().getParcelableArrayListExtra("listaSitios");
        listaBeacons=getIntent().getParcelableArrayListExtra("beacons");
        Bundle b= i.getExtras();
        service=(int) b.get("servicio");
        for(Beacon item: listaBeacons){
            valoresIds.add(item.id);
        }

    }

    private void cambiarImg(String imgUrl){
        try {
            ImageView img = (ImageView) findViewById(R.id.imagen_portada);
            new DownLoadImageTask(img).execute(imgUrl);
        }catch(Exception e){}
    }

    protected void onResume() {
        super.onResume();
        if (service == 0){

            beaconManager = new BeaconManager(getApplicationContext());
        EstimoteSDK.initialize(getApplicationContext(), "<teuchitlan-84i>", "<4f94bfb6df0d2b5ddff8c4d4b66ef73e>");
        beaconManager.setLocationListener(new BeaconManager.LocationListener() {
            @Override
            public void onLocationsFound(List<EstimoteLocation> beacons) {

                for (EstimoteLocation beacon : beacons) {
                    int indiceContenedor = valoresIds.indexOf(beacon.id.toString());
                    Log.d("becon detectado", beacon.id.toString());
                    if (indiceContenedor != -1) {
                        Log.d("detectado esta en bd : ", beacon.id.toString());
                        encontrarSitio(beacon.id.toString());
                        break;
                    }

                }

            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startLocationDiscovery();
            }
        });
    }
    else{   //entra directo desde el service

            Bundle b= getIntent().getExtras();
            sitioBeacon=(sitioHistorico)b.get("sitioEspecifico");
            fotos=sitioBeacon.arrayImagenes();
            actualizarUi(fotos,sitioBeacon);
        }


    }


    protected void onDestroy() {
        super.onDestroy();
        if(service==0) {
            beaconManager.disconnect();
        }
    }

    public void encontrarSitio(String id){
        for(sitioHistorico item: listaSitios){
            if(item.idBeacon.equals(id)){
                fotos=item.arrayImagenes();
                actualizarUi(fotos,item);
                break;
            }
        }
    }

    public void comprobarBeacon(String idBeacon){
        if(ban>=cantidadSnaps) {
            if (beaconsVisitados.isEmpty()) {
                if (service == 0) {
                   // beaconManager.disconnect();
                }

                beaconsVisitados.add(idBeacon);
                HashMap<String, Object> result = new HashMap<>();
                result.put("idBeacon", idBeacon);
                referencia.push().updateChildren(result);
                mostrarPopUp();
            } else {
                if (beaconsVisitados.indexOf(idBeacon) == -1) {
                    if (service == 0) {
                       // beaconManager.disconnect();
                    }
                    beaconsVisitados.add(idBeacon);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("idBeacon", idBeacon);
                    referencia.push().updateChildren(result);
                } else {

                }
            }
        }
    }

    public void mostrarPopUp(){

        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.pop_up);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("M");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }





    public void actualizarUi(ArrayList<String> imgs, sitioHistorico sitioH){
        ui=false;
        cambiarImg(imgs.get(0));
        TextView cultural=(TextView)findViewById(R.id.dato_cultural_text);
        TextView historico=(TextView)findViewById(R.id.dato_historico_text);
        cultural.setText(sitioH.datoCultural);
        historico.setText(sitioH.datoHistorico);

        HorizontalInfiniteCycleViewPager pager = (HorizontalInfiniteCycleViewPager)findViewById(R.id.horizontal_cycle);
        AdapterCarousel adapter = new AdapterCarousel(imgs,getBaseContext());
        Log.d("cantidad imagenes ",Integer.toString( adapter.getCount()));
        pager.setAdapter(adapter);

        String direccion="Teuchitlan/Users/"+user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        referencia=mDatabase.child(direccion+"/BeaconsVisitados");
        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cantidadSnaps=(int) dataSnapshot.getChildrenCount();
                ban=0;
                Log.d("cantidad de snaps ",Integer.toString(cantidadSnaps));
                if(dataSnapshot.exists()){

                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {

                        beaconsVisitados.add(snapChild.child("idBeacon").getValue().toString());
                        ban++;
                        comprobarBeacon(sitioH.idBeacon);
                    }
                }
                if(cantidadSnaps==0){
                    comprobarBeacon(sitioH.idBeacon);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

