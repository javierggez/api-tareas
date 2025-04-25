package com.apitareas.controladores;

import com.apitareas.entidades.Usuario;
import com.apitareas.seguridad.SolicitudAutenticacion;
import com.apitareas.seguridad.RespuestaAutenticacion;
import com.apitareas.seguridad.UtilJwt;
import com.apitareas.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/autenticacion")
public class ControladorAutenticacion {

    @Autowired
    private AuthenticationManager gestorAutenticacion;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private UtilJwt utilJwt;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody Usuario usuario) {
        try {
            servicioUsuario.registrarUsuario(usuario);
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<?> iniciarSesion(@Valid @RequestBody SolicitudAutenticacion solicitud) {
        try {
            gestorAutenticacion.authenticate(
                    new UsernamePasswordAuthenticationToken(solicitud.getNombreUsuario(), solicitud.getContrasena())
            );
            Usuario usuario = servicioUsuario.buscarPorNombreUsuario(solicitud.getNombreUsuario());
            String token = utilJwt.generarToken(usuario.getNombreUsuario(), usuario.getRol());
            return ResponseEntity.ok(new RespuestaAutenticacion(token));
        } catch (Exception e) {
            // Devolvemos un 401 Unauthorized con el mensaje esperado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas: " + e.getMessage());
        }
    }
}