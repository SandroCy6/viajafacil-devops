package com.viajafacil.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "itinerario")
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_itinerario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paquete", nullable = false)
    @JsonIgnore               // evita problemas LAZY en la salida de postman
    private Paquete paquete;

    @Column(nullable = false)
    private Integer dia;

    @Column(nullable = false, length = 200)
    private String actividad;

    @Column(name = "descripcion_itinerario", columnDefinition = "TEXT", nullable = true)
    private String descripcion;

    @Column(length = 150)
    private String lugar;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime hora_inicio;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime hora_fin;

    //orden dentro del día (por si no se usan horas)
    private Integer orden;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Column(nullable = false)
    private LocalDateTime fecha_creacion;

    @PrePersist
    public void prePersist() {
        if (fecha_creacion == null) {
            fecha_creacion = LocalDateTime.now();
        }
    }

    // ---- Getters / Setters ----
    public Long getId_itinerario() { return id_itinerario; }
    public void setId_itinerario(Long id_itinerario) { this.id_itinerario = id_itinerario; }

    public Paquete getPaquete() { return paquete; }
    public void setPaquete(Paquete paquete) { this.paquete = paquete; }

    public Integer getDia() { return dia; }
    public void setDia(Integer dia) { this.dia = dia; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public Integer getOrden() {return orden;}
    public void setOrden(Integer orden) {this.orden = orden;}

    public String getActividad() { return actividad; }
    public void setActividad(String actividad) { this.actividad = actividad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalTime getHora_inicio() { return hora_inicio; }
    public void setHora_inicio(LocalTime hora_inicio) { this.hora_inicio = hora_inicio; }

    public LocalTime getHora_fin() { return hora_fin; }
    public void setHora_fin(LocalTime hora_fin) { this.hora_fin = hora_fin; }

    public LocalDateTime getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(LocalDateTime fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    // Exponer id_paquete en el JSON sin cargar el objeto paquete
    @Transient
    @JsonProperty("id_paquete")
    public Long getIdPaquete() {
        return (paquete != null) ? paquete.getId_paquete() : null;
    }
}

