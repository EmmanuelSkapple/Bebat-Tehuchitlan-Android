package com.example.adan.teuchitlan;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

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
