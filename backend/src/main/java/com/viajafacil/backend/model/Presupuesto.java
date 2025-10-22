package com.viajafacil.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "presupuesto")
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_presupuesto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate fecha_inicio;

    @Column(nullable = false)
    private LocalDate fecha_fin;

    @Column(nullable = false)
    private Double presupuesto_estimado;

    public Presupuesto() {}

    public Presupuesto(Usuario usuario, String destino, LocalDate fecha_inicio, LocalDate fecha_fin, Double presupuesto_estimado) {
        this.usuario = usuario;
        this.destino = destino;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.presupuesto_estimado = presupuesto_estimado;
    }

    // Getters y Setters
    public Long getId_presupuesto() {
        return id_presupuesto;
    }

    public void setId_presupuesto(Long id_presupuesto) {
        this.id_presupuesto = id_presupuesto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public Double getPresupuesto_estimado() {
        return presupuesto_estimado;
    }

    public void setPresupuesto_estimado(Double presupuesto_estimado) {
        this.presupuesto_estimado = presupuesto_estimado;
    }
}
