package com.example.adan.teuchitlan;

/**
 * Created by adan on 9/04/18.
 */

public class sitioHistorico {
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

    public String toString(){
        return datoCultural+ " "+datoCurioso+ " "+idBeacon;
    }
}
