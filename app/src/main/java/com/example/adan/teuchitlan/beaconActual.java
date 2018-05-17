package com.example.adan.teuchitlan;

import android.content.Context;
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

    private DatabaseReference mDatabase, referencia;
    ArrayList<String> valoresIds=new ArrayList<String>();
    BeaconManager beaconManager;
    String imagenes;
    boolean ban=false;
    boolean banBeaconManager=true;
    private ExecutorService executor;
    boolean ui=true;
    Operaciones operacion=new Operaciones();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_beacon_actual);
        executor= Executors.newFixedThreadPool(2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        referencia= mDatabase.child("Teuchitlan/beacons");
       // cambiarImg();
        if(operacion.conectadoInternet(this)) {
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        Log.d("xxxxxxxxxxxxxxxxxxxx", snapChild.child("id").getValue().toString());

                        valoresIds.add(snapChild.child("id").getValue().toString());
                    }
                    ban=true;
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void cambiarImg(String imgUrl){
        try {
            ImageView img = (ImageView) findViewById(R.id.imagen_portada);
           // String imgUrl="https://cdn.images.express.co.uk/img/dynamic/20/590x/secondary/brigette-lundy-paine-atypical-1034740.jpg";
            new DownLoadImageTask(img).execute(imgUrl);
        }catch(Exception e){}
    }

    protected void onResume(){
        super.onResume();
        beaconManager = new BeaconManager(getApplicationContext());
        EstimoteSDK.initialize(getApplicationContext(), "<teuchitlan-84i>", "<4f94bfb6df0d2b5ddff8c4d4b66ef73e>");
        beaconManager.setLocationListener(new BeaconManager.LocationListener() {
            @Override
            public void onLocationsFound(List<EstimoteLocation> beacons) {

                if(ban && banBeaconManager) {
                    for (EstimoteLocation beacon : beacons) {
                        int indiceContenedor = valoresIds.indexOf(beacon.id.toString());
                        Log.d("becon detectado",beacon.id.toString());
                        Log.d(" el inidce es  : ", Integer.toString(indiceContenedor));
                        if (indiceContenedor != -1 ) {
                            Log.d("detectado esta en bd : ", beacon.id.toString());
                            obtenerLugarHistorico(valoresIds.get(indiceContenedor));

                        }

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
        imagenes="";
        Log.d("tag", "entro a obtener lugar" + idBeacon);
        if (operacion.conectadoInternet(this)) {

            final String idB = idBeacon;
            referencia = mDatabase.child("Teuchitlan/SitiosHistoricos");
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        Log.d("tag", idB+" entro a obtener lugar en onDataChange");
                        Log.d("tag",  "becon de lugar en DB "+snapChild.child("idBeacon").getValue().toString());

                        if (idB.equals(snapChild.child("idBeacon").getValue().toString())) {
                            Log.d("tag","los id son iguales");
                            sitioHistorico sh = new sitioHistorico(snapChild.child("datoCultural").getValue().toString(), snapChild.child("datoCurioso").getValue().toString(),
                                    snapChild.child("datoHistorico").getValue().toString(), snapChild.child("datoInteres").getValue().toString(), snapChild.child("id").getValue().toString(),
                                    snapChild.child("idBeacon").getValue().toString(), snapChild.child("imagenes").getValue().toString(),
                                    snapChild.child("key").getValue().toString(), snapChild.child("nombre").getValue().toString());
                            Log.d("zzzzzzzz", sh.toString());
                            imagenes = snapChild.child("imagenes").getValue().toString();
                            arrayImagenes(imagenes.toCharArray(),sh);

                        }
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }//termina if

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

