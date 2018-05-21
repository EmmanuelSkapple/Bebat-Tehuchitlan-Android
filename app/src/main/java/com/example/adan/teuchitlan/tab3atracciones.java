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

public class tab3atracciones extends Fragment {

    View v;
    RecyclerView myrecyclerview;
    ArrayList<Lugar> listaAtracciones = new ArrayList<Lugar>();
    ArrayList<datosLugares> datosLugaresList = new ArrayList<>();

    public tab3atracciones(){
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        v = inflater.inflate(R.layout.tab3atracciones, container, false);

        myrecyclerview = (RecyclerView) v.findViewById(R.id.tab3atracciones_recyclerview);
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(getContext(), datosLugaresList);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);

        return v;
    }

    public void onCreate(@Nullable Bundle saveInsteanceState) {

        super.onCreate(saveInsteanceState);

        Intent in = getActivity().getIntent();
        listaAtracciones = in.getParcelableArrayListExtra("atracciones");
        Log.d("cantidad de hoteles:", Integer.toString(listaAtracciones.size()));

        for(int i=0; i<listaAtracciones.size(); i++){
            datosLugaresList.add(new datosLugares(" "+listaAtracciones.get(i).nombre.toString(),
                                                       " "+listaAtracciones.get(i).id.toString(),
                                                  " "+listaAtracciones.get(i).imagenes.toString(),
                                                       " "+listaAtracciones.get(i).key.toString(),
                                                  " "+listaAtracciones.get(i).descripcion.toString(),
                                                        " "+listaAtracciones.get(i).tipo.toString(),
                                                    " "+listaAtracciones.get(i).ubicacion.toString()));
        }
    }
}
