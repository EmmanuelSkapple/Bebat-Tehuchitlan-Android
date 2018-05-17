package com.example.adan.teuchitlan;

/**
 * Created by robert on 5/9/18.
 */

public class lugaresAtracciones {
    private String descripcion;
    private int id;
    private String imagenes;
    private String key;
    private String nombre;
    private String tipo;
    private String ubicacion;

    public lugaresAtracciones() {}

    public lugaresAtracciones(String descripcion, int id, String imagenes, String key, String nombre, String tipo, String ubicacion) {
        this.descripcion = descripcion;
        this.id = id;
        this.imagenes = imagenes;
        this.key = key;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getId() {
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(int id) {
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
