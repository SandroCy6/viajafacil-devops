package com.viajafacil.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "¡Hola, el backend Spring Boot ESTAMOS EN PROCESO , PERO YA TENEMOS CONEXION A LA BSE DE DATOS! ";
    }
}
