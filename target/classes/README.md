# API REST para Gestión de Tareas (ApiTareas)

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-green) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue) ![JWT](https://img.shields.io/badge/JWT-Authentication-orange) ![Swagger](https://img.shields.io/badge/Swagger-Documentation-yellow)

## 📖 Descripción del Proyecto

**ApiTareas** es una API REST robusta diseñada para la gestión de tareas, construida con **Spring Boot 3.2.4** y **Java 17**. Permite a los usuarios registrarse, iniciar sesión, y realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre tareas. La autenticación está implementada con **JWT (JSON Web Tokens)** para garantizar la seguridad, y la API está documentada con **Swagger** para facilitar su uso.

Este proyecto fue desarrollado como parte de un ejercicio práctico para demostrar habilidades en desarrollo backend, incluyendo autenticación, autorización, manejo de bases de datos, pruebas automatizadas, y despliegue en la nube (AWS).

### 🎯 Características Principales

- **Autenticación y Autorización**: Registro e inicio de sesión de usuarios con JWT.
- **Gestión de Tareas**: Crear, listar, obtener, actualizar y eliminar tareas.
- **Seguridad**: Autenticación basada en tokens JWT y roles de usuario (`USER`).
- **Documentación**: API documentada con Swagger, accesible en `/swagger-ui.html`.
- **Base de Datos**: PostgreSQL para almacenamiento persistente.
- **Pruebas**: Pruebas unitarias e integrales con JUnit 5 y MockMvc.
- **Despliegue**: Preparada para despliegue en AWS (Elastic Beanstalk).

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 17, Spring Boot 3.2.4
- **Base de Datos**: PostgreSQL 16
- **Autenticación**: Spring Security con JWT (jjwt 0.12.5)
- **Documentación**: Swagger (springdoc-openapi 2.1.0)
- **Pruebas**: JUnit 5, Mockito, Spring Boot Test, Spring Security Test
- **Dependencias**:
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Spring Validation
    - Lombok
    - PostgreSQL Driver
- **Despliegue**: AWS Elastic Beanstalk

## 📂 Estructura del Proyecto

apitareas/
│
├── src/
│   ├── main/
│   │   ├── java/com/apitareas/
│   │   │   ├── controladores/       # Controladores REST (Autenticación y Tareas)
│   │   │   ├── dto/                # Objetos de transferencia de datos (DTOs)
│   │   │   ├── entidades/          # Entidades JPA (Usuario, Tarea)
│   │   │   ├── repositorios/       # Repositorios JPA para acceso a datos
│   │   │   ├── seguridad/          # Configuración de seguridad (JWT, autenticación)
│   │   │   └── servicios/          # Lógica de negocio (Usuario, Tarea)
│   │   └── resources/
│   │       ├── application.properties       # Configuración general
│   │       └── application-prod.properties  # Configuración para producción
│   └── test/
│       └── java/com/apitareas/     # Pruebas unitarias e integrales
└── pom.xml                         # Archivo de dependencias de Maven



## 🚀 Configuración y Ejecución Local

### Prerrequisitos

- **Java 17** instalado.
- **Maven** instalado.
- **PostgreSQL 16** instalado y corriendo.
- Un cliente HTTP como Postman o Insomnia para probar los endpoints.

### 1. Clonar el Repositorio

```bash
git clone https://github.com/javierggez/apitareas.git
cd apitareas