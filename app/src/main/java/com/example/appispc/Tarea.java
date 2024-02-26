package com.example.appispc;

public class Tarea {
    private long id;
    private String titulo;

    public Tarea(long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}