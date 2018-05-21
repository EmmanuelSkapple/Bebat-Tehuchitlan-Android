package com.example.adan.teuchitlan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adan on 20/05/18.
 */

public class progresoAdapter extends RecyclerView.Adapter<progresoAdapter.ViewHolder> {


    ArrayList<logrosModelo>listaLogros=new ArrayList<logrosModelo>();
    Context context;
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        TextView mTextView2;
        ImageView mImgtView;
        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.titulo_medalla);
            mTextView2 = (TextView) v.findViewById(R.id.descripcion_medalla);
            mImgtView = (ImageView) v.findViewById(R.id.imagen_medalla);
        }
    }

    public  progresoAdapter(Context contex,ArrayList<logrosModelo> lista){
        this.listaLogros=lista;
        this.context=contex;
    }


    @Override
    public progresoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_progreso, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(progresoAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(listaLogros.get(position).titulo);
        holder.mTextView2.setText(listaLogros.get(position).descripcion);
        Picasso.with(context).load(listaLogros.get(position).imagen).into(holder.mImgtView);

    }

    @Override
    public int getItemCount() {
        return listaLogros.size();
    }
}
