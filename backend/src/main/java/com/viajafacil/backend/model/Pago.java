package com.viajafacil.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    @JsonIgnore
    private Reserva reserva;

    //esto hace que lo exponga la columna , pero no lo toca , solo lo muestra en el postman
    @Column(name = "id_reserva", insertable = false, updatable = false)
    @JsonProperty("id_reserva")
    private Long idReservaFk;

    @Column(nullable = false)
    private Double monto;

    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodo; // Efectivo, Tarjeta , Yape , Plin , Transferencia a mi cuenta xd

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm") // salida JSON para probar en postman
    private LocalDateTime fecha_pago = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    private String estado = "confirmado"; // pendiente | confirmado | anulado

    //  Se ejecuta justo antes de insertar en la BD es decir la hora se inserta automaticamente
    @PrePersist
    public void prePersist() {
        if (this.fecha_pago == null) {
            this.fecha_pago = LocalDateTime.now();
        }
    }
    // Getters/Setters
    public Long getId_pago() { return id_pago; }
    public void setId_pago(Long id_pago) { this.id_pago = id_pago; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    public Long getIdReservaFk() { return idReservaFk; } // solo lectura en el postman (FK)
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public LocalDateTime getFecha_pago() { return fecha_pago; }
    public void setFecha_pago(LocalDateTime fecha_pago) { this.fecha_pago = fecha_pago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
