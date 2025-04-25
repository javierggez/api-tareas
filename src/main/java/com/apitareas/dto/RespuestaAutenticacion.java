package com.apitareas.dto;

import lombok.Data;

@Data
public class RespuestaAutenticacion {
    private String token;

    public RespuestaAutenticacion(String token) {
        this.token = token;
    }
}