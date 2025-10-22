package com.viajafacil.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "paquete")
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_paquete;

    private String descripcion;

    private String categoria; // alto, medio, bajo

    private Double precio_base;

    public Paquete() {}

    public Paquete(String descripcion, String categoria, Double precio_base) {
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio_base = precio_base;
    }

    // Getters y Setters
    public Long getId_paquete() {
        return id_paquete;
    }

    public void setId_paquete(Long id_paquete) {
        this.id_paquete = id_paquete;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPrecio_base() {
        return precio_base;
    }

    public void setPrecio_base(Double precio_base) {
        this.precio_base = precio_base;
    }
}
