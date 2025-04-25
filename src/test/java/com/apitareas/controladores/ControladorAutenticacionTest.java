package com.apitareas.controladores;

import com.apitareas.entidades.Usuario;
import com.apitareas.seguridad.SolicitudAutenticacion;
import com.apitareas.seguridad.RespuestaAutenticacion;
import com.apitareas.seguridad.UtilJwt;
import com.apitareas.servicios.ServicioUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControladorAutenticacionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager gestorAutenticacion;

    @MockBean
    private ServicioUsuario servicioUsuario;

    @MockBean
    private UtilJwt utilJwt;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrar_exito() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        usuario.setContrasena("password");
        usuario.setCorreo("test@ejemplo.com");
        usuario.setRol("USER");

        doNothing().when(servicioUsuario).registrarUsuario(any(Usuario.class));

        // Act & Assert
        mockMvc.perform(post("/autenticacion/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado con éxito"));
    }

    @Test
    void registrar_usuarioExistente_falla() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        usuario.setContrasena("password");
        usuario.setCorreo("test@ejemplo.com");
        usuario.setRol("USER");

        doThrow(new RuntimeException("El nombre de usuario ya está en uso"))
                .when(servicioUsuario).registrarUsuario(any(Usuario.class));

        // Act & Assert
        mockMvc.perform(post("/autenticacion/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error al registrar usuario: El nombre de usuario ya está en uso")));
    }

    @Test
    void iniciarSesion_exito() throws Exception {
        // Arrange
        SolicitudAutenticacion solicitud = new SolicitudAutenticacion();
        solicitud.setNombreUsuario("usuarioTest");
        solicitud.setContrasena("password");

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        usuario.setRol("USER");

        when(gestorAutenticacion.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("usuarioTest", null));
        when(servicioUsuario.buscarPorNombreUsuario("usuarioTest")).thenReturn(usuario);
        when(utilJwt.generarToken("usuarioTest", "USER")).thenReturn("token");

        // Act & Assert
        mockMvc.perform(post("/autenticacion/iniciar-sesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void iniciarSesion_credencialesInvalidas_falla() throws Exception {
        // Arrange
        SolicitudAutenticacion solicitud = new SolicitudAutenticacion();
        solicitud.setNombreUsuario("usuarioTest");
        solicitud.setContrasena("password");

        when(gestorAutenticacion.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/autenticacion/iniciar-sesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales inválidas: Bad credentials"));
    }
}