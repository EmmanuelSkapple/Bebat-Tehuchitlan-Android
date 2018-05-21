package com.example.adan.teuchitlan;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class progreso extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Profile usuarioFb;
    private DatabaseReference mDatabase, referencia;
    private int bandera=-1;
    private int cantidadSnaps=0;
    ArrayList<String> beaconsVisitados=new ArrayList<String>();
    ArrayList<logrosModelo>listaLogros=new ArrayList<logrosModelo>();
    ArrayList<logrosModelo>listaDesbloqueados=new ArrayList<logrosModelo>();
    RecyclerView mRecycler;
    progresoAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        Log.d("el usuario no", "registrado" + user);
        usuarioFb=Profile.getCurrentProfile();
        mDatabase = FirebaseDatabase.getInstance().getReference();  //se crean referencias a la base de datos
        referencia = mDatabase.child("Teuchitlan/Users/"+user.getUid()+"/BeaconsVisitados");
        listaLogros.add(new logrosModelo(R.string.logro1,R.string.descripcion_logro,R.drawable.primera));
        listaLogros.add(new logrosModelo(R.string.logro2,R.string.descripcion_logro2,R.drawable.dos));
        listaLogros.add(new logrosModelo(R.string.logro3,R.string.descripcion_logro3,R.drawable.tres));
        listaLogros.add(new logrosModelo(R.string.logro4,R.string.descripcion_logro4,R.drawable.cuatro));
        listaLogros.add(new logrosModelo(R.string.logro5,R.string.descripcion_logro5,R.drawable.cinco));




        if(new Operaciones().conectadoInternet(this)){    //se verifica que el dispositivo este conectado a internet

            referencia.addListenerForSingleValueEvent(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cantidadSnaps=(int) dataSnapshot.getChildrenCount();
                    bandera=0;
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapChild : dataSnapshot.getChildren()) {

                            beaconsVisitados.add(snapChild.child("idBeacon").getValue().toString());
                            bandera++;
                            construirArrays();
                        }
                    }
                    else{
                        construirArrays();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_progreso);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_progreso);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void construirArrays(){
        int index=-1;

        //solo sube hasta 3 beacons visitados
        if(bandera>=cantidadSnaps){
            if(cantidadSnaps ==1){
                listaDesbloqueados.add(listaLogros.get(0));
                index=1;
            }
            else if(cantidadSnaps==2){
                listaDesbloqueados.add(listaLogros.get(0));
                listaDesbloqueados.add(listaLogros.get(1));
                index=2;
            }
            else if( cantidadSnaps>2 && cantidadSnaps<=5){
                listaDesbloqueados.add(listaLogros.get(0));
                listaDesbloqueados.add(listaLogros.get(1));
                listaDesbloqueados.add(listaLogros.get(2));
                index=3;
            }

            mRecycler=(RecyclerView)findViewById(R.id.recyclerview_progreso);
            LinearLayoutManager linear= new LinearLayoutManager(this);
            mRecycler.setLayoutManager(linear);

            cargarDatos();
        }
    }

    public void cargarDatos(){
        if(mRecycler!=null){
            if(mAdapter==null){
                mAdapter=new progresoAdapter(this,listaDesbloqueados);
                mRecycler.setAdapter(mAdapter);
            }
            else {
                mAdapter.notifyDataSetChanged();
            }

        }

        CircleImageView c=(CircleImageView)findViewById(R.id.imagen_perfil_progreso);
        TextView t1=(TextView)findViewById(R.id.txt_usuario_progreso);
        Profile usuarioFb= Profile.getCurrentProfile();
        Picasso.with(this).load(usuarioFb.getProfilePictureUri(250,250)).into(c);
        t1.setText(usuarioFb.getFirstName()+ " "+usuarioFb.getLastName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(getApplicationContext(), mapaProgreso.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), progreso.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), beaconActual.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), queHacer.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(progreso.this,Login.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_progreso);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
