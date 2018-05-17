package com.example.adan.teuchitlan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adan on 8/05/18.
 */

public class Lugar implements Parcelable {

    String descripcion,id,imagenes,key,nombre,tipo,ubicacion;

    public Lugar(String descripcion,String id, String imagenes,String key, String nombre, String tipo, String ubicacion){
        this.descripcion=descripcion;
        this.id=id;
        this.imagenes=imagenes;
        this.key=key;
        this.nombre=nombre;
        this.tipo=tipo;
        this.ubicacion=ubicacion;
    }

    public Lugar(Parcel in){
        descripcion=in.readString();
        id=in.readString();
        imagenes=in.readString();
        key=in.readString();
        nombre=in.readString();
        tipo=in.readString();
        ubicacion=in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Lugar createFromParcel(Parcel in) {
                    return new Lugar(in);
                }

                @Override
                public Lugar[] newArray(int size) {
                    return new Lugar[size];
                }
            };

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(descripcion);
        parcel.writeString(id);
        parcel.writeString(imagenes);
        parcel.writeString(key);
        parcel.writeString(nombre);
        parcel.writeString(tipo);
        parcel.writeString(ubicacion);
    }
}
