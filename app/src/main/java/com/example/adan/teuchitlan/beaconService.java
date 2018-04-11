package com.example.adan.teuchitlan;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by adan on 20/03/18.
 */

public class beaconService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    static String RASTREO="rastreo";
    beaconHandler bh;
    public static void busquedaBeacon(Context contexto){
        Intent intent=new Intent(contexto,beaconService.class);
        intent.setAction(RASTREO);
        contexto.startService(intent);
    }

    public void onStartCommand(Intent intent){
    }

    private final class beaconHandler extends Handler{
        beaconHandler(Looper looper){
            super(looper);

        }
        public void handleMessage(Message msg){
            Log.i("xxx","esta corriendo el hilo de beacons :"+Thread.currentThread().getName());
        }
    }

    public void onCreate(){
        Log.i("service","creando hilo");
        HandlerThread background= new HandlerThread(
                "hiloRastreo"
        );
        background.start();
        bh=new beaconHandler(background.getLooper());
    }
}
