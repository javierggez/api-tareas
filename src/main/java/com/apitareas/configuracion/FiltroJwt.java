package com.apitareas.configuracion;

import com.apitareas.seguridad.UtilJwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class FiltroJwt extends OncePerRequestFilter {

    private final UtilJwt utilJwt;

    public FiltroJwt(UtilJwt utilJwt) {
        this.utilJwt = utilJwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest solicitud, HttpServletResponse respuesta, FilterChain cadena)
            throws ServletException, IOException {
        // Omitir el procesamiento para los endpoints de autenticación
        String path = solicitud.getRequestURI();
        if (path.startsWith("/autenticacion/") || path.startsWith("/swagger-ui/") || path.startsWith("/api-docs/")) {
            cadena.doFilter(solicitud, respuesta);
            return;
        }

        String encabezadoAutorizacion = solicitud.getHeader("Authorization");

        String nombreUsuario = null;
        String jwt = null;

        if (encabezadoAutorizacion != null && encabezadoAutorizacion.startsWith("Bearer ")) {
            jwt = encabezadoAutorizacion.substring(7);
            nombreUsuario = utilJwt.extraerNombreUsuario(jwt);
        }

        if (nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (utilJwt.validarToken(jwt)) {
                String rol = utilJwt.extraerRol(jwt);
                UsernamePasswordAuthenticationToken tokenAutenticacion =
                        new UsernamePasswordAuthenticationToken(
                                nombreUsuario,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol))
                        );
                tokenAutenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(solicitud));
                SecurityContextHolder.getContext().setAuthentication(tokenAutenticacion);
            }
        }
        cadena.doFilter(solicitud, respuesta);
    }
}