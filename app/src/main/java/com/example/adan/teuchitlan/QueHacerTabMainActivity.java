package com.example.adan.teuchitlan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QueHacerTabMainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, referencia;
    ArrayList <String> lugares = new ArrayList<>();


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        referencia = mDatabase.child("Teuchitlan/lugares");

        if (conectadoInternet()){
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que_hacer_tab_main);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter   = new ViewPagerAdapter(getSupportFragmentManager());

        //agrega los fragments
        adapter.AddFragment(new tab1hoteles(), "");
        adapter.AddFragment(new tab2restaurantes(), "");
        adapter.AddFragment(new tab3atracciones(), "");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hotel);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_restaurant);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_atracciones);

    }



    public boolean conectadoInternet() {

        ConnectivityManager cm;
        NetworkInfo ni;
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        boolean conexion = false;

        if (ni != null) {
            ConnectivityManager connManager1 = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            ConnectivityManager connManager2 = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
