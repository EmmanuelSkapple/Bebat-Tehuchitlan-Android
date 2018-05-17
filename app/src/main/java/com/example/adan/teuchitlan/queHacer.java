package com.example.adan.teuchitlan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class queHacer extends AppCompatActivity {

    ArrayList<Lugar> listaLugares=new ArrayList<Lugar>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_que_hacer);
        Intent i=this.getIntent();
        listaLugares=getIntent().getParcelableArrayListExtra("lugares");
    }
}
