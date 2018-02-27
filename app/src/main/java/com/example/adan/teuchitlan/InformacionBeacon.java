package com.example.adan.teuchitlan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InformacionBeacon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_beacon);
        Intent i=getIntent();
        String color=i.getStringExtra("beacon");
        Toast.makeText(this,"el color es "+color,Toast.LENGTH_SHORT).show();
        TextView txt=(TextView)findViewById(R.id.txt);
        ImageView foto=(ImageView)findViewById(R.id.imagenBeacon);
        if (color.toString().equals("menta")) {
            foto.setImageResource(R.drawable.beaconazul);
            txt.setText("Beacon menta");

        }
        else if(color.toString().equals("azul")){
            foto.setImageResource(R.drawable.beaconblue);
            txt.setText("Beacon azul");
        }

    }
}
