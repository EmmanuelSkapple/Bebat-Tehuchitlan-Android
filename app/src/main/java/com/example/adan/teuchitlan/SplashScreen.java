package com.example.adan.teuchitlan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity implements OnTaskCompleted{

    String s="";
    private OnTaskCompleted taskCompleted;
    private DatabaseReference mDatabase, referencia,referencia2,referencia3,referencia4;
    ArrayList<sitioHistorico> listaSitiosHistoricos=new ArrayList<sitioHistorico>();
    ArrayList<Beacon> beacons=new ArrayList<Beacon>();
    ArrayList<Lugar>lugares=new ArrayList<Lugar>();
    ArrayList<String>beaconsVisitados=new ArrayList<>();
    Operaciones o=new Operaciones();
    private int bandera=-1;
    private int bandera2=-1;
    private int bandera3=-1;
    private int cantidadSnaps=0;
    private int cantidadSnaps2=0;
    private int cantidadSnaps3=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Log.d("entro a ", "splash");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        try {
            if(o.conectadoInternet(this)){

            mDatabase = FirebaseDatabase.getInstance().getReference();
            referencia = mDatabase.child("Teuchitlan/SitiosHistoricos");
            referencia2 = mDatabase.child("Teuchitlan/beacons");
            referencia3 = mDatabase.child("Teuchitlan/lugares");
            referencia4=mDatabase.child("Teuchitlan/Users/"+user.getUid()+"/BeaconsVisitados");
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cantidadSnaps=(int) dataSnapshot.getChildrenCount();
                    bandera=0;
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        sitioHistorico sh = new sitioHistorico(snapChild.child("datoCultural").getValue().toString(), snapChild.child("datoCurioso").getValue().toString(),
                                snapChild.child("datoHistorico").getValue().toString(), snapChild.child("datoInteres").getValue().toString(), snapChild.child("id").getValue().toString(),
                                snapChild.child("idBeacon").getValue().toString(), snapChild.child("imagenes").getValue().toString(),
                                snapChild.child("key").getValue().toString(), snapChild.child("nombre").getValue().toString());
                        listaSitiosHistoricos.add(sh);
                        bandera++;
                        Log.d("lista en sync", "agregado a lista " + sh.nombre);
                        lanzarIntent();
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
                    cantidadSnaps2=(int) dataSnapshot.getChildrenCount();
                    bandera2=0;
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        Beacon b = new Beacon(snapChild.child("id").getValue().toString(), snapChild.child("referencia").getValue().toString(),
                                snapChild.child("tipo").getValue().toString(), snapChild.child("ubicacion").getValue().toString(), snapChild.child("zona").getValue().toString());
                        beacons.add(b);
                        bandera2++;
                        Log.d("sync beacons", "tomo " + b.id);
                        lanzarIntent();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            referencia3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("cantidad de snaps ", Long.toString(dataSnapshot.getChildrenCount()));
                    cantidadSnaps3=(int) dataSnapshot.getChildrenCount();
                    bandera3=0;
                    for (DataSnapshot snapChild : dataSnapshot.getChildren()) {
                        if(snapChild.exists()) {
                            Log.d("cantidad de snaps ", Long.toString(dataSnapshot.getChildrenCount()));
                            Lugar l = new Lugar(snapChild.child("descripcion").getValue().toString(), snapChild.child("id").getValue().toString(),
                                    snapChild.child("imagenes").getValue().toString(), snapChild.child("key").getValue().toString(), snapChild.child("nombre").getValue().toString(),
                                    snapChild.child("tipo").getValue().toString(), snapChild.child("ubicacion").getValue().toString());
                            lugares.add(l);
                            Log.d("lista en sync", "agregado a lista " + l.nombre);
                            bandera3++;
                            lanzarIntent();
                        }


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


        }
        else{
                Toast.makeText(this,"No estas conectado a internet",Toast.LENGTH_LONG);
            }

        }catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT);
        }
    }

    public void lanzarIntent(){


        if(bandera>=cantidadSnaps&&bandera2>=cantidadSnaps2&&bandera3>=cantidadSnaps3) {
            Log.d("for de beacon", "antes del intent");
            Log.d("contadores","sitios "+bandera+" snaps "+cantidadSnaps);
            Log.d("contadores","beacons "+bandera2+" snaps "+cantidadSnaps2);
            Log.d("contadores","lugares "+bandera3+" snaps "+cantidadSnaps3);
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            i.putParcelableArrayListExtra("beacons", beacons);
            i.putParcelableArrayListExtra("lugares",lugares);
            i.putParcelableArrayListExtra("sitios",listaSitiosHistoricos);


            startActivity(i);
        }
    }

    @Override
    public void onTaskCompleted(ArrayList<Beacon> listaB) {

    }
}


