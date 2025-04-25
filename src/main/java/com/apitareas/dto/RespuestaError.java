package com.apitareas.dto;

import lombok.Data;

@Data
public class RespuestaError {
    private String mensaje;
    private int codigo;

    public RespuestaError(String mensaje, int codigo) {
        this.mensaje = mensaje;
        this.codigo = codigo;
    }
}