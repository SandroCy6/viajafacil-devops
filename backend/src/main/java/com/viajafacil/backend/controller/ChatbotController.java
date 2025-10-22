
package com.viajafacil.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.viajafacil.backend.services.OpenAIService;
import com.viajafacil.backend.model.ConsultaChat;
import com.viajafacil.backend.repository.ConsultaChatRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatbot")  // Base URL: /chatbot
@CrossOrigin(origins = "*")
public class ChatbotController {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private ConsultaChatRepository consultaChatRepository;

    @PostMapping("/preguntar")  // Endpoint: POST /chatbot/preguntar
    public Map<String, Object> preguntarIA(
            @RequestParam Long usuarioId,
            @RequestParam String mensaje) {

        // Llama al servicio de IA
        String respuestaIA = openAIService.generarPlanViaje(mensaje);

        // Guardar en BD
        ConsultaChat consulta = new ConsultaChat();
        consulta.setUsuarioId(usuarioId);
        consulta.setMensajeUsuario(mensaje);
        consulta.setRespuestaIA(respuestaIA);
        consultaChatRepository.save(consulta);

        // Respuesta para frontend
        Map<String, Object> response = new HashMap<>();
        response.put("usuarioId", usuarioId);
        response.put("mensajeUsuario", mensaje);
        response.put("respuestaIA", respuestaIA);
        response.put("fecha", consulta.getFecha());

        return response;
    }

    // Ver historial de consultas de un usuario
    @GetMapping("/historial/{usuarioId}")
    public List<ConsultaChat> historial(@PathVariable Long usuarioId) {
        return consultaChatRepository.findByUsuarioId(usuarioId);
    }

}
