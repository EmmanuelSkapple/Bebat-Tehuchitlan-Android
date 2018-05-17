package com.example.adan.teuchitlan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by adan on 7/05/18.
 */

 public class Operaciones {


     public boolean conectadoInternet(Context c) {

        ConnectivityManager cm;
        NetworkInfo ni;
        cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        boolean conexion = false;

        if (ni != null) {
            ConnectivityManager connManager1 = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            ConnectivityManager connManager2 = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
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
