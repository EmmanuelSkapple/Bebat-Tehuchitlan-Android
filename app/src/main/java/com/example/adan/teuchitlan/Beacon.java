package com.example.adan.teuchitlan;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by adan on 27/03/18.
 */

public class Beacon implements Parcelable{
    String id;
    String referencia;
    String tipo;
    String ubicacion;
    String zona;
    ArrayList<Beacon> items;

    public Beacon(){

    }

    public Beacon(Parcel in){
        readFromParcel(in);
    }
    public Beacon(String id,String referencia,String tipo,String ubicacion, String zona){
        this.id=id;
        this.referencia=referencia;
        this.tipo=tipo;
        this.ubicacion=ubicacion;
        this.zona=zona;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Beacon createFromParcel(Parcel in) {
                    return new Beacon(in);
                }

                @Override
                public Beacon[] newArray(int size) {
                    return new Beacon[size];
                }
            };


    private void readFromParcel(Parcel in){
        id=in.readString();
        referencia=in.readString();
        tipo=in.readString();
        ubicacion=in.readString();
        zona=in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(referencia);
        parcel.writeString(tipo);
        parcel.writeString(ubicacion);
        parcel.writeString(zona);

    }
}
