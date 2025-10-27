# Plataforma de reserva de vuelos y agenda - ViajaFacil
Aplicación de escritorio con interfaz gráfica para la busqueda, filtrado, registro y reserva de vuelos de distintas aerolineas, con distintos precios, rutas y horarios. wa

## Tabla de contenidos
1. [Descripción](#Descripción)
2. [Características](#características)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Código Detallado](#Código-Detallado)
5. [Requisitos](#requisitos)
6. [Configuración y ejecución](#configuración-y-ejecución)
7. [Tecnologías utilizadas](#tecnologías-utilizadas)
8. [Autores](#autores)
9. [Licencia](#licencia)

## Descripción
Este proyecto fue desarrollado como parte del curso **INTEGRADOR 1: SISTEMAS - SOFTWARE**.
El sistema permite la busqueda, mediante filtrados o general, de distintos vuelos, ya sean individuales o en paquetes, para su registro y reserva. Estos vuelos se recopilan de distintas aerolineas afileadas a la empresa, esto con el objetivo de lograr una variedad que enriquezca las opciones que nuestros clientes puedan encontrar.

El principal problema que tratamos de resolver es el que muchas aerolineas tienen al momento de expandir sus operaciones a campos más virtuales. Para esto se propuso el presente proyecto, como una oportunidad de **centralizar** todo lo que las aerolineas tienen para ofrecer, mejorando así, tanto su difusión como ventas.

## Características
- Arquitectura MVC (Modelo, Vista y Controlador)
- Interfaz gráfica hecha usando html, css y jsp.
- Chatbot para ayudar al usuario en dudas y busquedas sencillas.
- Validación de datos.
- Persistencia de datos (Un ejemplo sería las reservas realizadas).
- Exportación de datos(Como las boletas de venta, historiales, etc) a archivos externos, como pdf (En proceso de integración :V)

## Estructura del Proyecto
```
src/
│──main/
│   ├java/
│   │ └com/viajafacil/backend
│   │  ├config/
│   │  │└──SecurityConfig.java
│   │  ├controller/
│   │  │ ├ChatbotController.java
│   │  │ ├HelloController.java
│   │  │ ├LoginController.java
│   │  │ ├PaqueteController.java
│   │  │ ├PresupuestoController.java
│   │  │ ├RegisterController.java
│   │  │ ├ReservaController.java
│   │  │ └UsuarioController.java
│   │  ├model/
│   │  │ ├Categoría.java
│   │  │ ├ConsultaChat.java
│   │  │ ├Paquete.java
│   │  │ ├Presupuesto.java
│   │  │ ├Reserva.java
│   │  │ └Usuario.java
│   │  ├repository/
│   │  │ ├ConsultaChatRepository.java
│   │  │ ├PaqueteRepository.java
│   │  │ ├PresupuestoRepository.java
│   │  │ ├ReservaRepository.java
│   │  │ └UsuarioRepository.java
│   │  ├services/
│   │  │ └OpenAIService.java
│   │  └BackendApplication.java
│   └resources/
│     └application.properties
└──test
   └java/
    └com/viajafacil/backend
       └BackendApplicationTests.java   
```
## Código Detallado

wa

## Requisitos
- **Java 21** o superior  
- **Maven 3.9+**  
- **IntelliJ IDEA** o cualquier IDE compatible con Spring Boot  
- **PostgreSQL** instalado y configurado  

---

### Dependencias principales
El proyecto utiliza las siguientes librerías gestionadas mediante Maven:

| Dependencia | Descripción |
|--------------|-------------|
| `spring-boot-starter-web` | Permite construir APIs RESTful y manejar peticiones HTTP. |
| `spring-boot-starter-data-jpa` | Implementa JPA/Hibernate para el manejo de la base de datos. |
| `spring-boot-starter-security` | Añade autenticación y control de acceso a la aplicación. |
| `postgresql` | Driver JDBC para conectar la aplicación a una base de datos PostgreSQL. |
| `lombok` | Simplifica el código eliminando la necesidad de escribir getters, setters, constructores, etc. |

---

### Dependencias para pruebas
| Dependencia | Descripción |
|--------------|-------------|
| `spring-boot-starter-test` | Framework para realizar pruebas unitarias e integradas. |
| `spring-security-test` | Soporte para pruebas de seguridad (autenticación/autorización). |

## Configuración y ejecución
o si osi

## Tecnologías utilizadas

| Categoría | Tecnología| 
| --------- | ----------|
| Lenguaje principal | **Java 21** |
| Framework | **Springboot 3.5.6** |
| Gestor de dependencias | **Maven** |
| Base de datos | **Postgre SQL** | 
| ORM / Persistencia | **JPA / Hibernate** |
| Seguridad | **Spring Security** |
| Utilidades | **Lombok** |
| Entorno de desarrollo | **IntelliJ IDEA** |
| Pruebas | **Spring Boot Test / JUnit 5** |

## Autores

- Benyamin Nilo Sotelo Taboada : Lider de equipo, desarrollador de backend

- Sandro Raphael Díaz Maguiña : Jugador profesional de Left 4 Dead, Alias Bebanaytor, desarrollador de SQL y apartado de administración

- Fabrizzio Giovanni Prada Salas : Futuro lider de la escena, desarrollador de Frontend y documentación.

## Licencia
Este proyecto se distribuye bajo la licencia MIT.  
Consulta el archivo LICENSE para más información.
