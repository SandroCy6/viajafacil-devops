package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Buscar usuario por correo
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
