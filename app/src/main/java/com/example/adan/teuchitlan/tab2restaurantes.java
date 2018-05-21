package com.example.adan.teuchitlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;

/**
 * Created by robert on 4/30/18.
 */
public class tab2restaurantes extends Fragment{

    View v;
    RecyclerView myrecyclerview;
    ArrayList<Lugar> listaRestaurantes = new ArrayList<Lugar>();
    ArrayList<datosLugares> datosLugaresList = new ArrayList<>();


    public tab2restaurantes(){
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        v = inflater.inflate(R.layout.tab2restaurantes, container, false);

        myrecyclerview = (RecyclerView) v.findViewById(R.id.tab2restaurantes_recyclerview);
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(getContext(), datosLugaresList);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);
        return v;
    }


    public void onCreate(@Nullable Bundle saveInsteanceState) {

        super.onCreate(saveInsteanceState);

        Intent in = getActivity().getIntent();
        listaRestaurantes = in.getParcelableArrayListExtra("restaurantes");
        Log.d("cantidad de hoteles:", Integer.toString(listaRestaurantes.size()));

        for(int i=0; i<listaRestaurantes.size(); i++){
            datosLugaresList.add(new datosLugares(" "+listaRestaurantes.get(i).nombre.toString(),
                                                       " "+listaRestaurantes.get(i).id.toString(),
                                                  " "+listaRestaurantes.get(i).imagenes.toString(),
                                                       " "+listaRestaurantes.get(i).key.toString(),
                                                 " "+listaRestaurantes.get(i).descripcion.toString(),
                                                       " "+listaRestaurantes.get(i).tipo.toString(),
                                                   " "+listaRestaurantes.get(i).ubicacion.toString()));
        }
    }
}



