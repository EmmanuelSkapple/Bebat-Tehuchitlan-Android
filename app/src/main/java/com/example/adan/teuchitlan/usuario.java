package com.example.adan.teuchitlan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adan on 18/05/18.
 */

public class usuario {
    String id,nivel,nombre,edad,email,genero,pass="",numero;

    public  usuario(){

    }
    public  usuario(String id,String nombre,String edad,String email,String numero){
        this.id=id;
        this.nivel=nivel;
        this.nombre=nombre;
        this.edad=edad;
        this.email=email;
        this.numero=numero;
        this.genero=genero;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nivel", nivel);
        result.put("nombre", nombre);
        result.put("email", email);
        result.put("edad",edad);
        result.put("numero", numero);

        return result;
    }
}
