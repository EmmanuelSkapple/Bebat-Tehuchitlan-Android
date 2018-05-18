package com.example.adan.teuchitlan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.observation.region.RegionUtils;
import com.estimote.coresdk.observation.utils.Proximity;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class beaconActual extends AppCompatActivity {


    ArrayList<String> valoresIds=new ArrayList<String>();
    ArrayList<Beacon> listaBeacons=new ArrayList<Beacon>();
    ArrayList<sitioHistorico>listaSitios=new ArrayList<sitioHistorico>();
    ArrayList<String>fotos=new ArrayList<String>();
    sitioHistorico sitioBeacon;
    BeaconManager beaconManager;
    String imagenes;
    boolean ui=true;
    int service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_beacon_actual);
        Intent i=getIntent();
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
           // String imgUrl="https://cdn.images.express.co.uk/img/dynamic/20/590x/secondary/brigette-lundy-paine-atypical-1034740.jpg";
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



    public void arrayImagenes(char[] recibirArray,sitioHistorico sitio){
        String StringN="";
        Log.d("entro a deparar","imagenes");
        int aux=0;
        ArrayList<String> ArrayFg=new ArrayList<String>();
        for (int i = 0; i < recibirArray.length; i++) {
            if(recibirArray[i] =='~'){
                for (int j = i+1; j < recibirArray.length; j++) {
                    if(j==100||j==185||j==189){
                        Log.d("el for de j "," va en "+j+"de "+recibirArray.length+", su letra es "+recibirArray[j] );
                    }
                    aux=j;
                    if(recibirArray[j]!='~'){
                        StringN += recibirArray[j];
                    }
                    else if (recibirArray[j]=='~'&&j!=0||j+1==recibirArray.length) {
                        Log.d("entro al else","para separar imagen j es "+j+" y letra es "+recibirArray[j]);
                        ArrayFg.add(StringN);
                        StringN="";
                    }
                }
                Log.d("el for j"," termino y el valor de aux es "+aux+" el valor de length "+recibirArray.length);

                if(aux==recibirArray.length-1){
                    ArrayFg.add(StringN);
                    Log.d("completo", "el algoritmo de imagenes");
                    break;
                }
            }
        }

        //banBeaconManager=true;

        if(ui){
            actualizarUi(ArrayFg,sitio);
        }
    }

    public void actualizarUi(ArrayList<String> imgs, sitioHistorico sitioH){
        ui=false;
        cambiarImg(imgs.get(0));
        TextView cultural=(TextView)findViewById(R.id.dato_cultural_text);
        TextView historico=(TextView)findViewById(R.id.dato_historico_text);
        cultural.setText(sitioH.datoCultural);
        historico.setText(sitioH.datoHistorico);
    }
}

