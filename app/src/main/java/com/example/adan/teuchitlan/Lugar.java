package com.example.adan.teuchitlan;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

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

    public ArrayList<String> arrayImagenes(){
        char[ ] recibirArray=imagenes.toCharArray();
        String StringN="";
        Log.d("entro a deparar","imagenes");
        int aux=0;
        ArrayList<String> ArrayFg=new ArrayList<String>();
        for (int i = 0; i < recibirArray.length; i++) {
            if(recibirArray[i] =='~'){
                for (int j = i+1; j < recibirArray.length; j++) {
                    if(j==100||j==185||j==189){
                        Log.d("el for de j "," va en "+j+"de "+recibirArray.length+", su letra es "+recibirArray[j] );
                    }
                    aux=j;
                    if(recibirArray[j]!='~'){
                        StringN += recibirArray[j];
                    }
                    else if (recibirArray[j]=='~'&&j!=0||j+1==recibirArray.length) {
                        Log.d("entro al else","para separar imagen j es "+j+" y letra es "+recibirArray[j]);
                        ArrayFg.add(StringN);
                        StringN="";
                    }
                }
                Log.d("el for j"," termino y el valor de aux es "+aux+" el valor de length "+recibirArray.length);

                if(aux==recibirArray.length-1){
                    ArrayFg.add(StringN);
                    Log.d("completo", "el algoritmo de imagenes");
                    return  ArrayFg;
                }
            }
        }
        return ArrayFg;

    }
}
