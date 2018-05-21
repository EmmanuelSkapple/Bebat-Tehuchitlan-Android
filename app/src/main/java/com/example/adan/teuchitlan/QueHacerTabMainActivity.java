package com.example.adan.teuchitlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class QueHacerTabMainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, referencia;
    ArrayList<Lugar> listaLugares = new ArrayList<Lugar>();

    ArrayList<Lugar> listaHoteles = new ArrayList<Lugar>();
    ArrayList<Lugar> listaRestaurantes = new ArrayList<Lugar>();
    ArrayList<Lugar> listaAtracciones = new ArrayList<Lugar>();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i=this.getIntent();
        listaLugares=getIntent().getParcelableArrayListExtra("lugares");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que_hacer_tab_main);


        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter   = new ViewPagerAdapter(getSupportFragmentManager());


        //***********************************************
        Log.d("tamano del array"," "+listaLugares.size());


        for(int counter=0; counter<listaLugares.size(); counter++){

            if(listaLugares.get(counter).tipo.equals("Hotel")){

                listaHoteles.add(listaLugares.get(counter));

            }

            else if(listaLugares.get(counter).tipo.equals("Restaurante")){

                listaRestaurantes.add(listaLugares.get(counter));

            }

            else if(listaLugares.get(counter).tipo.equals("Atraccion")){

                listaAtracciones.add(listaLugares.get(counter));

            }
        }


        Log.d("hoteles:"," "+listaHoteles.size());
        Log.d("restaurantes:"," "+listaRestaurantes.size());
        Log.d("atracciones:"," "+listaAtracciones.size());









        //agrega los fragment
        getIntent().putParcelableArrayListExtra("hoteles", listaHoteles);
        adapter.AddFragment(new tab1hoteles(), "");

        getIntent().putParcelableArrayListExtra("restaurantes", listaRestaurantes);
        adapter.AddFragment(new tab2restaurantes(), "");

        getIntent().putParcelableArrayListExtra("atracciones", listaAtracciones);
        adapter.AddFragment(new tab3atracciones(), "");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hotel);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_restaurant);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_atracciones);


    }



}
