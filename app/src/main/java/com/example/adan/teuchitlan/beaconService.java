package com.example.adan.teuchitlan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adan on 20/03/18.
 */

public class beaconService extends Service {
    ArrayList<sitioHistorico> lista=new ArrayList<sitioHistorico>();
    ArrayList<Beacon>listaBeacons=new ArrayList<Beacon>();
    ArrayList<String> idBeacons=new ArrayList<String>();
    BeaconManager beaconManager;
    Bitmap img;
    private boolean notificationAlreadyShown = false;


    public void onCreate(){
        Log.i("service","creando hilo");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent in=new Intent(getApplicationContext(),Login.class);
            startActivity(in);
            this.onDestroy();
        }


    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service","entrar a OnBind");

        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        try{

            Intent i=intent;
            listaBeacons=i.getParcelableArrayListExtra("beacons");
            lista=i.getParcelableArrayListExtra("listaSitios");
            for(Beacon item: listaBeacons){
                idBeacons.add(item.id);
            }

                beaconManager = new BeaconManager(getApplicationContext());
        EstimoteSDK.initialize(getApplicationContext(), "<teuchitlan-84i>", "<4f94bfb6df0d2b5ddff8c4d4b66ef73e>");
            Log.d("entro a onStart ", "comienza el scanner");
            beaconManager.setLocationListener(new BeaconManager.LocationListener() {
            @Override
            public void onLocationsFound(List<EstimoteLocation> beacons) {
                for (EstimoteLocation beacon : beacons) {
                    int indiceContenedor = idBeacons.indexOf(beacon.id.toString());
                    Log.d("becon detectado", beacon.id.toString()+" indice "+indiceContenedor);
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


    }catch(Exception e){

    }
        return flags;
    }

    public void encontrarSitio(String id){
        Log.d("encontrar sitio", "id "+id);
        for(sitioHistorico item: lista){
            if(item.idBeacon.equals(id)){

                showNotification("beacon encontrado", "bienvenido a "+item.nombre, "azul",item);
                break;
            }
        }
    }

    public void showNotification(String title, String message,  String color, sitioHistorico sitio) {
        long[] v = {400,700};

        if(notificationAlreadyShown){return ;}
        Intent notifyIntent = new Intent(this, beaconActual.class);
        notifyIntent.putParcelableArrayListExtra("listaSitios",lista);
        notifyIntent.putParcelableArrayListExtra("beacons",listaBeacons);
        notifyIntent.putExtra("sitioEspecifico",sitio );
        notifyIntent.putExtra("servicio",1);
        Log.d("el nombre es ",message);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setVibrate(v);
        builder.setLargeIcon (BitmapFactory.decodeResource(getResources(), R.drawable.mazamitla));
        builder.setStyle(new NotificationCompat.InboxStyle().addLine(sitio.datoCurioso).setBigContentTitle("Bienvenido a "+sitio.nombre).setSummaryText("descubriste lugar mágico"));
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Will display the notification in the notification bar
        notificationManager.notify(0, builder.build());
        notificationAlreadyShown = true;


    }






}
