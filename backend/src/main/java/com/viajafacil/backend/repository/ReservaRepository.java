package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE r.usuario.id_usuario = :idUsuario")
    List<Reserva> findByUsuarioId(@Param("idUsuario") Long idUsuario);
}
