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

public class tab1hoteles extends Fragment {

     View v;

     RecyclerView myrecyclerview;
     ArrayList<Lugar> listaHoteles = new ArrayList<Lugar>();
     ArrayList<datosLugares> datosLugaresList = new ArrayList<>();

    public tab1hoteles(){}


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){

        v = inflater.inflate(R.layout.tab1hoteles, container, false);

        myrecyclerview = (RecyclerView) v.findViewById(R.id.tab1hotel_recyclerview);
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(getContext(), datosLugaresList);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInsteanceState) {

        super.onCreate(saveInsteanceState);

        Intent in = getActivity().getIntent();
        listaHoteles = in.getParcelableArrayListExtra("hoteles");
        Log.d("cantidad de hoteles:", Integer.toString(listaHoteles.size()));

        for(int i=0; i<listaHoteles.size(); i++){
            datosLugaresList.add(new datosLugares( " "+listaHoteles.get(i).nombre.toString(),
                                                        " "+listaHoteles.get(i).id.toString(),
                                                  " "+listaHoteles.get(i).arrayImagenes().get(0),
                                                        " "+listaHoteles.get(i).key.toString(),
                                                " "+listaHoteles.get(i).descripcion.toString(),
                                                     " "+listaHoteles.get(i).tipo.toString(),
                                                 " "+listaHoteles.get(i).ubicacion.toString()));

        }
    }

}
