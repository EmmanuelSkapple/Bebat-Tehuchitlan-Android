package com.example.adan.teuchitlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class informacionLugar extends AppCompatActivity {


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
    TextView tv_imagenes;
    TextView tv_key;
    TextView tv_tipo;
    TextView tv_ubicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        tv_nombre = (TextView) findViewById(R.id.nombre_informacion_lugar_id);
        tv_descripcion = (TextView) findViewById(R.id.descripcion_informacion_lugar_id);

        tv_nombre.setText(nombre);
        tv_descripcion.setText(descripcion);


        Log.d("recibio el nombre", nombre);
        Log.d("recibio la descripcion", descripcion);
        Log.d("recibio el id", id);
        Log.d("recibio las imagenes", imagenes);
        Log.d("recibio el key", key);
        Log.d("recibio el tipo", tipo);
        Log.d("recibio la ubicacion", ubicacion);


    }
}
