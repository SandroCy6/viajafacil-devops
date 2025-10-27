package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query("SELECT p FROM Pago p WHERE p.reserva.usuario.id_usuario = :usuarioId ORDER BY p.fecha_pago DESC")
    List<Pago> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("""
           SELECT p FROM Pago p
            WHERE (:metodo IS NULL OR p.metodo = :metodo)
                     AND p.fecha_pago >= COALESCE(:desde, p.fecha_pago)
                     AND p.fecha_pago <= COALESCE(:hasta, p.fecha_pago)
                   ORDER BY p.fecha_pago DESC
           """)
    List<Pago> filter(
            @Param("metodo") String metodo,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
}
