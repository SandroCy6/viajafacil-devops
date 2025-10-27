package com.viajafacil.backend.controller;

import com.viajafacil.backend.model.Usuario;
import com.viajafacil.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
// Permite inyectar dependencias automáticamente (como el repositorio)
import org.springframework.web.bind.annotation.*;
// Habilita las anotaciones para crear endpoints REST (Get, Post

import java.util.HashMap;
import java.util.Map;
// Estructuras para crear respuestas en formato clave-valor
import java.util.Optional;

@RestController
// Indica que esta clase atiende peticiones REST y devuelve JSON
@RequestMapping("/login")
@CrossOrigin(origins = "*")
// Permite conexiones desde cualquier origen es decir solo /login
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // DTO para recibir los datos de login
    public static class LoginRequest {
        private String correo;
        private String contrasena;

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }

    @GetMapping
    public String test() {
        return "LoginController esta activo, falta conectar al Frontend ";
    }

    @PostMapping
    // Endpoint para procesar el inicio de sesión
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(loginRequest.getCorreo());
        // Busca el usuario por su correo

        Map<String, Object> response = new HashMap<>();
        // Crea un mapa para enviar la respuesta al frontend

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuario.getContrasena().equals(loginRequest.getContrasena())) {
                response.put("status", "success");
                response.put("message", "Login exitoso");
                response.put("usuario", usuario);
            } else {
                response.put("status", "error");
                response.put("message", "Contraseña incorrecta");
            }
        } else {
            response.put("status", "error");
            response.put("message", "Usuario no encontrado");
        }

        return response;
        // Devuelve el resultado al frontend en formato JSON
    }
}

