package com.viajafacil.backend.controller;

import com.viajafacil.backend.model.Reserva;
import com.viajafacil.backend.model.Usuario;
import com.viajafacil.backend.model.Paquete;
import com.viajafacil.backend.repository.ReservaRepository;
import com.viajafacil.backend.repository.UsuarioRepository;
import com.viajafacil.backend.repository.PaqueteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PaqueteRepository paqueteRepository;

    //  Crear nueva reserva (acepta JSON o parámetros en URL)
    @PostMapping
    public Map<String, Object> crearReserva(
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idPaquete,
            @RequestBody(required = false) Map<String, Object> body) {

        Map<String, Object> response = new HashMap<>();

        // Permitir recibir datos desde JSON o desde la URL
        if (body != null) {
            if (idUsuario == null && body.get("idUsuario") != null)
                idUsuario = Long.valueOf(body.get("idUsuario").toString());
            if (idPaquete == null && body.get("idPaquete") != null)
                idPaquete = Long.valueOf(body.get("idPaquete").toString());
        }

        if (idUsuario == null || idPaquete == null) {
            response.put("status", "error");
            response.put("message", "Debe enviar idUsuario y idPaquete.");
            return response;
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        Optional<Paquete> paqueteOpt = paqueteRepository.findById(idPaquete);

        if (usuarioOpt.isPresent() && paqueteOpt.isPresent()) {
            Reserva reserva = new Reserva();
            reserva.setUsuario(usuarioOpt.get());
            reserva.setPaquete(paqueteOpt.get());
            reserva.setFecha_reserva(LocalDate.now());
            reserva.setEstado_reserva("pendiente");

            Reserva nuevaReserva = reservaRepository.save(reserva);

            //  Información personalizada para mostrar en el frontend
            Map<String, Object> reservaInfo = new LinkedHashMap<>();
            reservaInfo.put("id_reserva", nuevaReserva.getId_reserva());
            reservaInfo.put("nombre_usuario", nuevaReserva.getUsuario().getNombre());
            reservaInfo.put("nombre_paquete", nuevaReserva.getPaquete().getDescripcion());
            reservaInfo.put("fecha_reserva", nuevaReserva.getFecha_reserva());
            reservaInfo.put("estado_reserva", nuevaReserva.getEstado_reserva());

            response.put("status", "success");
            response.put("message", " Reserva creada exitosamente.");
            response.put("reserva", reservaInfo);
        } else {
            response.put("status", "error");
            response.put("message", "Usuario o paquete no encontrado.");
        }

        return response;
    }

    //  Listar todas las reservas (solo para admin)
    @GetMapping
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    //  Listar reservas de un usuario (para su cuenta personal)
    @GetMapping("/usuario/{idUsuario}")
    public List<Map<String, Object>> listarReservasPorUsuario(@PathVariable Long idUsuario) {
        List<Reserva> reservas = reservaRepository.findByUsuarioId(idUsuario);
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Reserva r : reservas) {
            Map<String, Object> reservaMap = new LinkedHashMap<>();
            reservaMap.put("id_reserva", r.getId_reserva());
            reservaMap.put("descripcion_paquete", r.getPaquete().getDescripcion());
            reservaMap.put("categoria", r.getPaquete().getCategoria());
            reservaMap.put("precio_base", r.getPaquete().getPrecio_base());
            reservaMap.put("fecha_reserva", r.getFecha_reserva());
            reservaMap.put("estado_reserva", r.getEstado_reserva());
            lista.add(reservaMap);
        }
        return lista;
    }


    //  Actualizar estado de reserva (confirmar / cancelar)
    @PutMapping("/{id}")
    public Map<String, Object> actualizarEstadoReserva(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {

        Map<String, Object> response = new HashMap<>();
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.setEstado_reserva(nuevoEstado.toLowerCase());
            reservaRepository.save(reserva);

            response.put("status", "success");
            response.put("message", "Estado de reserva actualizado correctamente.");
        } else {
            response.put("status", "error");
            response.put("message", "Reserva no encontrada.");
        }

        return response;
    }

    //  Eliminar reserva
    @DeleteMapping("/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        if (reservaRepository.existsById(id)) {
            reservaRepository.deleteById(id);
            return "Reserva con id " + id + " eliminada correctamente.";
        } else {
            return "Reserva con id " + id + " no encontrada.";
        }
    }
}
