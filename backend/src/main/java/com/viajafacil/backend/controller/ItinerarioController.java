package com.viajafacil.backend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.viajafacil.backend.model.Itinerario;
import com.viajafacil.backend.model.Paquete;
import com.viajafacil.backend.repository.ItinerarioRepository;
import com.viajafacil.backend.repository.PaqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/itinerarios")
@CrossOrigin(origins = "*")
public class ItinerarioController {

    @Autowired private ItinerarioRepository itinerarioRepository;
    @Autowired private PaqueteRepository paqueteRepository;

    // ---------- DTOs de entrada ----------
    public static class CrearItinerarioReq {
        public Long paqueteId;             // requerido
        public Integer dia;                // requerido (Día 1, 2, ...)
        public String actividad;           // requerido
        public String descripcion;         // opcional

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        public LocalTime horaInicio;       // opcional

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        public LocalTime horaFin;          // opcional

        public Integer orden; //(si no envían, se autoasigna)
    }

    public static class ActualizarItinerarioReq {
        public Long paqueteId;             // opcional (cambiar paquete del ítem)
        public Integer dia;                // opcional
        public String actividad;           // opcional
        public String descripcion;         // opcional

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        public LocalTime horaInicio;       // opcional

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        public LocalTime horaFin;          // opcional

        public Integer orden;
    }

    @GetMapping
    public List<Itinerario> listarTodos() {
        return itinerarioRepository.findAll();
    }

    // ---------- CREATE ----------
    @PostMapping
    public Map<String, Object> crear(@RequestBody CrearItinerarioReq req) {
        Map<String, Object> resp = new LinkedHashMap<>();

        if (req == null || req.paqueteId == null || req.dia == null ||
                req.actividad == null || req.actividad.isBlank()) {
            resp.put("status", "error");
            resp.put("message", "paqueteId, dia y actividad son obligatorios");
            return resp;
        }

        var paqueteOpt = paqueteRepository.findById(req.paqueteId);
        if (paqueteOpt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Paquete no encontrado");
            return resp;
        }

        var paquete = paqueteOpt.get();

        Itinerario it = new Itinerario();
        it.setPaquete(paquete);
        it.setDia(req.dia);
        it.setActividad(req.actividad.trim());
        it.setDescripcion(req.descripcion);
        it.setHora_inicio(req.horaInicio);
        it.setHora_fin(req.horaFin);

        // Si no envían orden y tampoco hay hora, autoincrementa orden
        if (req.orden != null) {
            it.setOrden(req.orden);
        } else if (req.horaInicio == null) {
            Integer max = itinerarioRepository.maxOrdenEnDia(req.paqueteId, req.dia);
            it.setOrden(max + 1);
        }

        Itinerario guardado = itinerarioRepository.save(it);

        resp.put("status", "success");
        resp.put("message", "Actividad registrada en el itinerario");
        resp.put("itinerario", guardado);
        return resp;
    }

    // -------- LISTAR por paquete (y opcional por día) --------
    @GetMapping("/paquete/{idPaquete}")
    public List<Itinerario> listarPorPaquete(
            @PathVariable Long idPaquete,
            @RequestParam(required = false) Integer dia // <-- NUEVO filtro por día
    ) {
        return itinerarioRepository.listarPorPaqueteYDiaOrdenado(idPaquete, dia);
    }

    // -------- GET por id --------
    @GetMapping("/{id}")
    public Map<String, Object> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> resp = new LinkedHashMap<>();
        var opt = itinerarioRepository.findById(id);
        if (opt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Itinerario no encontrado");
            return resp;
        }
        resp.put("status", "success");
        resp.put("itinerario", opt.get());
        return resp;
    }

    // -------- UPDATE --------
    @PutMapping("/{id}")
    public Map<String, Object> actualizar(@PathVariable Long id,
                                          @RequestBody ActualizarItinerarioReq req) {
        Map<String, Object> resp = new LinkedHashMap<>();
        var opt = itinerarioRepository.findById(id);
        if (opt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Itinerario no encontrado");
            return resp;
        }

        Itinerario it = opt.get();

        if (req.paqueteId != null && !req.paqueteId.equals(
                it.getPaquete() != null ? it.getPaquete().getId_paquete() : null)) {
            var p = paqueteRepository.findById(req.paqueteId);
            if (p.isEmpty()) {
                resp.put("status", "error");
                resp.put("message", "Paquete no encontrado");
                return resp;
            }
            it.setPaquete(p.get());
        }

        if (req.actividad != null && !req.actividad.isBlank()) it.setActividad(req.actividad.trim());
        if (req.descripcion != null) it.setDescripcion(req.descripcion);
        if (req.horaInicio != null) it.setHora_inicio(req.horaInicio);
        if (req.horaFin != null) it.setHora_fin(req.horaFin);
        if (req.orden != null) it.setOrden(req.orden);

        Itinerario actualizado = itinerarioRepository.save(it);
        resp.put("status", "success");
        resp.put("message", "Itinerario actualizado");
        resp.put("itinerario", actualizado);
        return resp;
    }

    // -------- DELETE --------
    @DeleteMapping("/{id}")
    public Map<String, Object> eliminar(@PathVariable Long id) {
        Map<String, Object> resp = new LinkedHashMap<>();
        var opt = itinerarioRepository.findById(id);
        if (opt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Itinerario no encontrado");
            return resp;
        }
        itinerarioRepository.delete(opt.get());
        resp.put("status", "success");
        resp.put("message", "Itinerario eliminado correctamente");
        return resp;
    }

    @PutMapping("/{id}/lugar")
    public ResponseEntity<?> actualizarLugar(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String lugar = (body != null) ? body.get("lugar") : null;
        if (lugar == null || lugar.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El campo 'lugar' es obligatorio"
            ));
        }

        return itinerarioRepository.findById(id)
                .map(it -> {
                    it.setLugar(lugar);
                    itinerarioRepository.save(it);
                    return ResponseEntity.ok(Map.of(
                            "status", "success",
                            "message", "Lugar actualizado",
                            "itinerario", it
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "Itinerario no encontrado"
                )));
    }
}
