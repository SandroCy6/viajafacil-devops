package com.viajafacil.backend.controller;

import com.viajafacil.backend.model.Pago;
import com.viajafacil.backend.model.Reserva;
import com.viajafacil.backend.repository.PagoRepository;
import com.viajafacil.backend.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired private PagoRepository pagoRepository;
    @Autowired private ReservaRepository reservaRepository;

    // DTO de entrada para crear
    public static class PagoRequest {
        public Long reservaId;
        public Double monto;
        public String metodo;    // Efectivo/Tarjeta/Yape/Plin/Transferencia
        public String estado;    // opcional (por defecto "confirmado" en la entidad)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
        public LocalDateTime fechaPago; // opcional (si no viene, @PrePersist pone now)
    }

    // DTO de entrada para actualizar parcial
    public static class PagoUpdateRequest {
        public Double monto;   // opcional
        public String metodo;  // opcional (se normaliza a MAYÚSCULAS)
        public String estado;  // opcional
    }


    @PostMapping
    public Map<String, Object> registrarPago(@RequestBody PagoRequest req) {
        Map<String, Object> resp = new LinkedHashMap<>();

        if (req == null || req.reservaId == null || req.monto == null || req.metodo == null || req.metodo.isBlank()) {
            resp.put("status", "error");
            resp.put("message", "reservaId, monto y metodo son obligatorios");
            return resp;
        }

        Optional<Reserva> resOpt = reservaRepository.findById(req.reservaId);
        if (resOpt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Reserva no encontrada");
            return resp;
        }

        Pago p = new Pago();
        p.setReserva(resOpt.get());
        p.setMonto(req.monto);
        p.setMetodo(req.metodo.toUpperCase());     // normalización
        if (req.estado != null && !req.estado.isBlank()) p.setEstado(req.estado);
        if (req.fechaPago != null) p.setFecha_pago(req.fechaPago); // si no viene, @PrePersist la genera

        Pago guardado = pagoRepository.save(p);

        resp.put("status", "success");
        resp.put("message", "Pago registrado");
        resp.put("pago", guardado);
        return resp;
    }



    // GET por id (útil para verificar uno puntual)
    @GetMapping("/{id}")
    public Map<String, Object> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> resp = new LinkedHashMap<>();
        Optional<Pago> opt = pagoRepository.findById(id);
        if (opt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Pago no encontrado");
            return resp;
        }
        resp.put("status", "success");
        resp.put("pago", opt.get());
        return resp;
    }

    // Historial de pagos por usuario
    @GetMapping("/historial/usuario/{usuarioId}")
    public List<Pago> historialPorUsuario(@PathVariable Long usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    // Filtrar por método
    @GetMapping
    public List<Pago> filtrar(
            @RequestParam(required = false) String metodo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        String m = (metodo == null || metodo.isBlank()) ? null : metodo.toUpperCase(); // normalizado
        return pagoRepository.filter(m, desde, hasta); // usa COALESCE en el repo
    }


    @PutMapping("/{id}")
    public Map<String, Object> actualizarPago(@PathVariable Long id,
                                              @RequestBody PagoUpdateRequest req) {
        Map<String, Object> resp = new LinkedHashMap<>();

        Optional<Pago> opt = pagoRepository.findById(id);
        if (opt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Pago no encontrado");
            return resp;
        }

        Pago p = opt.get();

        if (req.monto != null) p.setMonto(req.monto);
        if (req.metodo != null && !req.metodo.isBlank()) p.setMetodo(req.metodo.toUpperCase());
        if (req.estado != null && !req.estado.isBlank()) p.setEstado(req.estado);

        Pago actualizado = pagoRepository.save(p);

        resp.put("status", "success");
        resp.put("message", "Pago actualizado");
        resp.put("pago", actualizado);
        return resp;
    }


    @DeleteMapping("/{id}")
    public Map<String, Object> eliminarPago(@PathVariable Long id) {
        Map<String, Object> resp = new LinkedHashMap<>();

        Optional<Pago> opt = pagoRepository.findById(id);
        if (opt.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "Pago no encontrado");
            return resp;
        }

        pagoRepository.delete(opt.get());

        resp.put("status", "success");
        resp.put("message", "Pago eliminado correctamente");
        return resp;
    }
}
