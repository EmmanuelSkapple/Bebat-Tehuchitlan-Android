package com.example.adan.teuchitlan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.estimote.coresdk.common.internal.utils.L;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by adan on 8/05/18.
 */

public class DataSyncFb extends AsyncTask<Void,Integer,Void> {

    private Context context;
    String s="";
    private OnTaskCompleted taskCompleted;
    private DatabaseReference mDatabase, referencia,referencia2,referencia3;
    ArrayList<sitioHistorico> listaSitiosHistoricos=new ArrayList<sitioHistorico>();
    ArrayList<Beacon> beacons=new ArrayList<Beacon>();
    ArrayList<Lugar>lugares=new ArrayList<Lugar>();
    Operaciones op=new Operaciones();


    public  DataSyncFb(Context context){
        this.context=context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        referencia = mDatabase.child("Teuchitlan/SitiosHistoricos");
        referencia2= mDatabase.child("Teuchitlan/beacons");
        referencia3=mDatabase.child("Teuchitlan/lugares");
    }
    protected void  onPreExecute(){
        super.onPreExecute();
        Log.d("entro a sync", "entroo");
    }

    @Override
    protected Void doInBackground(Void... voids) {


            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                            sitioHistorico sh = new sitioHistorico(snapChild.child("datoCultural").getValue().toString(), snapChild.child("datoCurioso").getValue().toString(),
                                    snapChild.child("datoHistorico").getValue().toString(), snapChild.child("datoInteres").getValue().toString(), snapChild.child("id").getValue().toString(),
                                    snapChild.child("idBeacon").getValue().toString(), snapChild.child("imagenes").getValue().toString(),
                                    snapChild.child("key").getValue().toString(), snapChild.child("nombre").getValue().toString());
                            listaSitiosHistoricos.add(sh);
                            Log.d("lista en sync", "agregado a lista "+sh.nombre);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //beacons
        referencia2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        Beacon b=new Beacon(snapChild.child("id").getValue().toString(),snapChild.child("referencia").getValue().toString(),
                                snapChild.child("tipo").getValue().toString(),snapChild.child("ubicacion").getValue().toString(),snapChild.child("zona").getValue().toString());
                        beacons.add(b);
                        Log.d("sync beacons","tomo "+b.id );
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        referencia3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapChild: dataSnapshot.getChildren()){
                    Lugar l=new Lugar(snapChild.child("descripcion").getValue().toString(),snapChild.child("id").getValue().toString(),
                            snapChild.child("imagenes").getValue().toString(),snapChild.child("key").getValue().toString(),snapChild.child("nombre").getValue().toString(),
                            snapChild.child("tipo").getValue().toString(),snapChild.child("ubicacion").getValue().toString());
                    lugares.add(l);
                }
                Log.d("for del terer","antes del intent");
                Intent i = new Intent(context, MainActivity.class);
                i.putParcelableArrayListExtra("beacons", beacons);
                context.startActivity(i);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return null;
    }

    protected  void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);


    }
}
