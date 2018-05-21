package com.example.adan.teuchitlan;

/**
 * Created by robert on 5/18/18.
 */

public class datosLugares {

        private String nombre;
        private String descripcion;
        private String id;
        private String imagenes;
        private String key;
        private String tipo;
        private String ubicacion;

    public datosLugares() {
    }

    public datosLugares(String nombre, String id, String imagenes, String key, String descripcion, String tipo, String ubicacion) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.id = id;
        this.imagenes = imagenes;
        this.key = key;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }


    //GETTERS

    public String getDescripcion() {
        return descripcion;
    }

    public String getId() {
        return id;
    }

    public String getImagenes() {
        return imagenes;
    }

    public String getKey() {
        return key;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }


    //SETTERS


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImagenes(String imagenes) {
        this.imagenes = imagenes;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
