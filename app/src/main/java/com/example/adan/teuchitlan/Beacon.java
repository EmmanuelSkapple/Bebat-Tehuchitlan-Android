package com.example.adan.teuchitlan;

/**
 * Created by adan on 27/03/18.
 */

public class Beacon {
    String id;
    String referencia;
    String tipo;
    String ubicacion;
    int zona;

    public Beacon(){

    }
    public Beacon(String id,String referencia,String tipo,String ubicacion, int zona){
        this.id=id;
        this.referencia=referencia;
        this.tipo=tipo;
        this.ubicacion=ubicacion;
        this.zona=zona;
    }
}
