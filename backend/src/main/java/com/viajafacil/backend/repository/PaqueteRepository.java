package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    List<Paquete> findByCategoria(String categoria);
}
