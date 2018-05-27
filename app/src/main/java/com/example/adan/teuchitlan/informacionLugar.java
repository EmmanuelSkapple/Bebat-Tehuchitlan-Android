package com.example.adan.teuchitlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class informacionLugar extends AppCompatActivity implements OnMapReadyCallback {


    String nombre;
    String descripcion;
    String id;
    String imagenes;
    String key;
    String tipo;
    String ubicacion;

    TextView tv_nombre;
    TextView tv_descripcion;
    TextView tv_id;
    ImageView iv_imagenes;
    TextView tv_key;
    TextView tv_tipo;
    TextView tv_ubicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        Intent intent = getIntent();

        nombre = intent.getExtras().getString("nombre");
        descripcion = intent.getExtras().getString("descripcion");
        id = intent.getExtras().getString("id");
        imagenes = intent.getExtras().getString("imagenes");
        key = intent.getExtras().getString("key");
        tipo = intent.getExtras().getString("tipo");
        ubicacion = intent.getExtras().getString("ubicacion");



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_lugar);

        //inicializa el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa_informacion_lugar_id);
        mapFragment.getMapAsync(this);

        tv_nombre = (TextView) findViewById(R.id.nombre_informacion_lugar_id);
        tv_descripcion = (TextView) findViewById(R.id.descripcion_informacion_lugar_id);
        iv_imagenes = (ImageView) findViewById(R.id.imagen_lugar_id);

        tv_nombre.setText(nombre);
        tv_descripcion.setText(descripcion);
        new DownLoadImageTask(iv_imagenes).execute(imagenes);


        Log.d("recibio el nombre", nombre);
        Log.d("recibio la descripcion", descripcion);
        Log.d("recibio el id", id);
        Log.d("recibio las imagenes", imagenes);
        Log.d("recibio el key", key);
        Log.d("recibio el tipo", tipo);
        Log.d("recibio la ubicacion", ubicacion);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        String [] coordenadas = ubicacion.split(",");

        Double Latitud = Double.parseDouble(coordenadas[0]);
        Double Longitud = Double.parseDouble(coordenadas[1]);

        LatLng ubicacion = new LatLng( Latitud, Longitud);
        googleMap.addMarker(new MarkerOptions().position(ubicacion)
                .title(nombre));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15.0f));
    }
}
