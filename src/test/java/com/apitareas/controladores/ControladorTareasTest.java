package com.apitareas.controladores;

import com.apitareas.dto.SolicitudTarea;
import com.apitareas.entidades.Tarea;
import com.apitareas.entidades.Usuario;
import com.apitareas.seguridad.UtilJwt;
import com.apitareas.servicios.ServicioTarea;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControladorTareasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioTarea servicioTarea;

    @MockBean
    private UtilJwt utilJwt;

    @Autowired
    private ObjectMapper objectMapper;

    private Tarea tarea;
    private SolicitudTarea solicitudTarea;

    @BeforeEach
    void setUp() {
        solicitudTarea = new SolicitudTarea();
        solicitudTarea.setTitulo("Tarea Test");
        solicitudTarea.setDescripcion("Descripción Test");
        solicitudTarea.setCompletada(false);

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Tarea Test");
        tarea.setDescripcion("Descripción Test");
        tarea.setCompletada(false);

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        tarea.setUsuario(usuario);
    }

    @Test
    @WithMockUser(username = "usuarioTest", roles = "USER")
    void crearTarea_exito() throws Exception {
        // Arrange
        when(servicioTarea.crearTarea(any(SolicitudTarea.class), eq("usuarioTest"))).thenReturn(tarea);

        // Act & Assert
        mockMvc.perform(post("/tareas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudTarea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Tarea Test"));
    }

    @Test
    @WithMockUser(username = "usuarioTest", roles = "USER")
    void listarTareas_exito() throws Exception {
        // Arrange
        Page<Tarea> paginaTareas = new PageImpl<>(Collections.singletonList(tarea));
        when(servicioTarea.listarTareas(eq("usuarioTest"), any(PageRequest.class))).thenReturn(paginaTareas);

        // Act & Assert
        mockMvc.perform(get("/tareas")
                        .param("pagina", "0")
                        .param("tamano", "10")
                        .param("ordenarPor", "fechaCreacion")
                        .param("direccion", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Tarea Test"));
    }

    @Test
    @WithMockUser(username = "usuarioTest", roles = "USER")
    void obtenerTarea_exito() throws Exception {
        // Arrange
        when(servicioTarea.obtenerTarea(1L, "usuarioTest")).thenReturn(tarea);

        // Act & Assert
        mockMvc.perform(get("/tareas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Tarea Test"));
    }

    @Test
    @WithMockUser(username = "usuarioTest", roles = "USER")
    void actualizarTarea_exito() throws Exception {
        // Arrange
        when(servicioTarea.actualizarTarea(eq(1L), any(SolicitudTarea.class), eq("usuarioTest"))).thenReturn(tarea);

        // Act & Assert
        mockMvc.perform(put("/tareas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudTarea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Tarea Test"));
    }

    @Test
    @WithMockUser(username = "usuarioTest", roles = "USER")
    void eliminarTarea_exito() throws Exception {
        // Arrange
        doNothing().when(servicioTarea).eliminarTarea(1L, "usuarioTest");

        // Act & Assert
        mockMvc.perform(delete("/tareas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tarea eliminada con éxito"));
    }
}