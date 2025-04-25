package com.apitareas.seguridad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaAutenticacion {

    private String token;

    public RespuestaAutenticacion(String token) {
        this.token = token;
    }
}