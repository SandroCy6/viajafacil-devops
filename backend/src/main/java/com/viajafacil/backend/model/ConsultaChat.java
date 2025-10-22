
package com.viajafacil.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

@Entity
@Table(name = "consultas_chat")
public class ConsultaChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private String mensajeUsuario;

    @Column(columnDefinition = "TEXT")
    private String respuestaIA;


    private LocalDateTime fecha = LocalDateTime.now();

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getMensajeUsuario() { return mensajeUsuario; }
    public void setMensajeUsuario(String mensajeUsuario) { this.mensajeUsuario = mensajeUsuario; }

    public String getRespuestaIA() { return respuestaIA; }
    public void setRespuestaIA(String respuestaIA) { this.respuestaIA = respuestaIA; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
