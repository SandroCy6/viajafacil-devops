package com.viajafacil.backend.repository;

import com.viajafacil.backend.model.ConsultaChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaChatRepository extends JpaRepository<ConsultaChat, Long> {
    List<ConsultaChat> findByUsuarioId(Long usuarioId);
}
