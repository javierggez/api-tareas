package com.apitareas.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class SolicitudAutenticacion {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}