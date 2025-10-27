package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
// Proporciona métodos CRUD listos
import org.springframework.stereotype.Repository;
// Marca esta interfaz como un repositorio de Spring

import java.util.Optional;
// Permite manejar resultados que pueden o no existir (evita null)

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Hereda todos los métodos CRUD de JpaRepository

    // Buscar usuario por correo
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    // Verifica si existe un usuario con ese correo
}
