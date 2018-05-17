package com.example.adan.teuchitlan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adan on 20/03/18.
 */

public class beaconService extends Service {
    ArrayList<sitioHistorico> lista=new ArrayList<sitioHistorico>();
    BeaconManager beaconManager;
    Bitmap img;
    private boolean notificationAlreadyShown = false;


    public void onCreate(){
        Log.i("service","creando hilo");
    onStartCommand();

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onStartCommand() {
        try{
        beaconManager = new BeaconManager(getApplicationContext());
        EstimoteSDK.initialize(getApplicationContext(), "<teuchitlan-84i>", "<4f94bfb6df0d2b5ddff8c4d4b66ef73e>");
            Log.d("entro a onStart ", "comienza el scanner");
            beaconManager.setLocationListener(new BeaconManager.LocationListener() {
           /* URL url = new URL("https://firebasestorage.googleapis.com/v0/b/teuchiapp.appspot.com/o/teuchitlan%2FSitiosHistoricos%2F45d03659c581aae20463a91b45e1e6b9.png?alt=media&token=4e6f81a1-bc0a-430f-a2dc-9ebad48e3cab");
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());*/
            @Override
            public void onLocationsFound(List<EstimoteLocation> beacons) {
                for (EstimoteLocation beacon : beacons) {
                    // int indiceContenedor = valoresIds.indexOf(beacon.id.toString());
                    Log.d("becon detectado", beacon.id.toString());
                    showNotification("beacon encontrado", "bienvenido a ","azul" );
                    //  Log.d(" el inidce es  : ", Integer.toString(indiceContenedor));
                    //  if (indiceContenedor != -1 ) {
                    //   Log.d("detectado esta en bd : ", beacon.id.toString());
                    //  obtenerLugarHistorico(valoresIds.get(indiceContenedor));

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

    }

    public void showNotification(String title, String message,  String color) {

        if (notificationAlreadyShown) { return; }

        Intent notifyIntent = new Intent(this, beaconActual.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
               // .setLargeIcon(bMap)
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
