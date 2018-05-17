package com.example.adan.teuchitlan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adan on 9/04/18.
 */

public class sitioHistorico implements Parcelable{
    String datoCultural,datoCurioso,datoHistorico, datoInteres,id,idBeacon,imagenes,key,nombre;
    public sitioHistorico(String datoCultural,String datoCurioso,String datoHistorico,String datoInteres,String id, String idBeacon,String imagenes,String key,String nombre){
        this.datoCultural=datoCultural;
        this.datoCurioso=datoCurioso;
        this.datoHistorico=datoHistorico;
        this.datoInteres=datoInteres;
        this.id=id;
        this.idBeacon=idBeacon;
        this.imagenes=imagenes;
        this.key=key;
        this.nombre=nombre;
    }

    public  sitioHistorico(Parcel in){

        datoCultural=in.readString();
        datoCurioso=in.readString();
        datoHistorico=in.readString();
        datoInteres=in.readString();
        id=in.readString();
        idBeacon=in.readString();
        imagenes=in.readString();
        key=in.readString();
        nombre=in.readString();
    }

    public String toString(){
        return datoCultural+ " "+datoCurioso+ " "+idBeacon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public sitioHistorico createFromParcel(Parcel in) {
                    return new sitioHistorico(in);
                }

                @Override
                public sitioHistorico[] newArray(int size) {
                    return new sitioHistorico[size];
                }
            };

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(datoCultural);
        parcel.writeString(datoCurioso);
        parcel.writeString(datoHistorico);
        parcel.writeString(datoInteres);
        parcel.writeString(id);
        parcel.writeString(idBeacon);
        parcel.writeString(imagenes);
        parcel.writeString(key);
        parcel.writeString(nombre);
    }
}
