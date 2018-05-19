package com.example.adan.teuchitlan;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QueHacerTabMainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, referencia;
    ArrayList<Lugar> listaLugares=new ArrayList<Lugar>();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i=this.getIntent();
        listaLugares=getIntent().getParcelableArrayListExtra("lugares");

        Log.d("entro a tabQue hacer","enviara intent");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que_hacer_tab_main);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter   = new ViewPagerAdapter(getSupportFragmentManager());

        //agrega los fragment
        getIntent().putParcelableArrayListExtra("listaHoteles",listaLugares);
        adapter.AddFragment(new tab1hoteles(), "");
        adapter.AddFragment(new tab2restaurantes(), "");
        adapter.AddFragment(new tab3atracciones(), "");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hotel);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_restaurant);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_atracciones);

    }



}
