package com.viajafacil.backend.controller;

import com.viajafacil.backend.model.Usuario;
import com.viajafacil.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public static class RegisterRequest {
        private String nombre;
        private String apellido;
        private String correo;
        private String contrasena;
        private String telefono;

        // getters y setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest req) {
        Map<String, Object> response = new HashMap<>();

        // Validaciones básicas
        if (req.getCorreo() == null || req.getCorreo().trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Correo es requerido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (req.getContrasena() == null || req.getContrasena().trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Contraseña es requerida");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Verificar si correo ya existe
        if (usuarioRepository.existsByCorreo(req.getCorreo())) {
            response.put("status", "error");
            response.put("message", "Correo ya registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Crear entidad Usuario y guardar
        Usuario usuario = new Usuario();
        usuario.setNombre(req.getNombre());
        usuario.setApellido(req.getApellido());
        usuario.setCorreo(req.getCorreo());


        usuario.setContrasena(req.getContrasena());
        usuario.setTelefono(req.getTelefono());

        Usuario guardado = usuarioRepository.save(usuario);

        response.put("status", "success");
        response.put("message", "Usuario registrado");
        response.put("usuario", guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
