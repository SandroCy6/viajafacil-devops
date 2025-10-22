package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    @Query("SELECT p FROM Presupuesto p WHERE p.usuario.id_usuario = :idUsuario")
    List<Presupuesto> findByUsuarioId(@Param("idUsuario") Long idUsuario);
}

