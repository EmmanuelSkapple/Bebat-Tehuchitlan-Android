package com.example.adan.teuchitlan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by robert on 5/18/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<datosLugares> mDatosLugares;

    public RecyclerViewAdapter(Context mContext, List<datosLugares> mDatosHoteles) {
        this.mContext = mContext;
        this.mDatosLugares = mDatosHoteles;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.cardview_item_lugar_opcion2, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);

        

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.nombre.setText(mDatosLugares.get(position).getNombre());
        new DownLoadImageTask(holder.img).execute(mDatosLugares.get(position).getImagenes());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, informacionLugar.class);

                //pasar la informacion del lugar seleccionado por aqui
                intent.putExtra("nombre", mDatosLugares.get(position).getNombre());
                intent.putExtra("descripcion", mDatosLugares.get(position).getDescripcion());
                intent.putExtra("id", mDatosLugares.get(position).getId());
                intent.putExtra("imagenes", mDatosLugares.get(position).getImagenes());
                intent.putExtra("key", mDatosLugares.get(position).getKey());
                intent.putExtra("tipo", mDatosLugares.get(position).getTipo());
                intent.putExtra("ubicacion", mDatosLugares.get(position).getUbicacion());

                //lanza el activity
                mContext.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatosLugares.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        CardView cardView;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombre_lugar_id2);
            img=(ImageView) itemView.findViewById(R.id.image_item_lugar);
            cardView = (CardView) itemView.findViewById(R.id.cardview_item_lugar_opcio2_id);

        }


    }
}
