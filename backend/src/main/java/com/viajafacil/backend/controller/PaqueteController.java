package com.viajafacil.backend.controller;

import com.viajafacil.backend.model.Paquete;
import com.viajafacil.backend.repository.PaqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paquetes")
@CrossOrigin(origins = "http://localhost:4200") // si tu frontend usa otro puerto, cámbialo
public class PaqueteController {

    @Autowired
    private PaqueteRepository paqueteRepository;

    //  GET: listar todos los paquetes
    @GetMapping
    public List<Paquete> getAllPaquetes() {
        return paqueteRepository.findAll();
    }

    //  POST: crear nuevo paquete
    @PostMapping
    public Paquete createPaquete(@RequestBody Paquete paquete) {
        // Normalizar categoría
        if (paquete.getCategoria() != null) {
            paquete.setCategoria(paquete.getCategoria().toLowerCase());
        }
        return paqueteRepository.save(paquete);
    }

    //  GET: obtener paquete por ID
    @GetMapping("/{id}")
    public Paquete getPaqueteById(@PathVariable Long id) {
        return paqueteRepository.findById(id).orElse(null);
    }

    //  PUT: actualizar paquete
    @PutMapping("/{id}")
    public Paquete updatePaquete(@PathVariable Long id, @RequestBody Paquete detallesPaquete) {
        Optional<Paquete> paqueteExistente = paqueteRepository.findById(id);

        if (paqueteExistente.isPresent()) {
            Paquete paquete = paqueteExistente.get();
            paquete.setDescripcion(detallesPaquete.getDescripcion());
            paquete.setCategoria(detallesPaquete.getCategoria());
            paquete.setPrecio_base(detallesPaquete.getPrecio_base());
            return paqueteRepository.save(paquete);
        } else {
            return null; // podrías reemplazar por ResponseEntity con 404
        }
    }

    //  DELETE: eliminar paquete
    @DeleteMapping("/{id}")
    public String deletePaquete(@PathVariable Long id) {
        if (paqueteRepository.existsById(id)) {
            paqueteRepository.deleteById(id);
            return "Paquete con id " + id + " eliminado correctamente.";
        } else {
            return "Paquete con id " + id + " no encontrado.";
        }
    }
}
