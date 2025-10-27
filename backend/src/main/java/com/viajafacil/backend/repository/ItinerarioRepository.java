package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    @Query("""
       SELECT i
       FROM Itinerario i
       WHERE i.paquete.id_paquete = :idPaquete
         AND (:dia IS NULL OR i.dia = :dia)
       ORDER BY i.dia ASC,
                i.hora_inicio ASC NULLS LAST,
                COALESCE(i.orden, 0) ASC
       """)
    List<Itinerario> listarPorPaqueteYDiaOrdenado(Long idPaquete, Integer dia);

    @Query("""
           SELECT COALESCE(MAX(i.orden), 0)
           FROM Itinerario i
           WHERE i.paquete.id_paquete = :idPaquete AND i.dia = :dia
           """)
    Integer maxOrdenEnDia(Long idPaquete, Integer dia);
}
