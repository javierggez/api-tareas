package com.apitareas.servicios;

import com.apitareas.dto.SolicitudTarea;
import com.apitareas.entidades.Tarea;
import com.apitareas.entidades.Usuario;
import com.apitareas.repositorios.RepositorioTarea;
import com.apitareas.repositorios.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioTareaTest {

    @Mock
    private RepositorioTarea repositorioTarea;

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @InjectMocks
    private ServicioTarea servicioTarea;

    private Usuario usuario;
    private Tarea tarea;
    private SolicitudTarea solicitudTarea;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        usuario.setRol("USER");

        solicitudTarea = new SolicitudTarea();
        solicitudTarea.setTitulo("Tarea Test");
        solicitudTarea.setDescripcion("Descripción Test");
        solicitudTarea.setCompletada(false);

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Tarea Test");
        tarea.setDescripcion("Descripción Test");
        tarea.setCompletada(false);
        tarea.setUsuario(usuario);
    }

    @Test
    void crearTarea_exito() {
        // Arrange
        when(repositorioUsuario.findByNombreUsuario("usuarioTest")).thenReturn(usuario);
        when(repositorioTarea.save(any(Tarea.class))).thenReturn(tarea);

        // Act
        Tarea resultado = servicioTarea.crearTarea(solicitudTarea, "usuarioTest");

        // Assert
        assertNotNull(resultado);
        assertEquals("Tarea Test", resultado.getTitulo());
        assertEquals("usuarioTest", resultado.getUsuario().getNombreUsuario());
        verify(repositorioTarea).save(any(Tarea.class));
    }

    @Test
    void listarTareas_exito() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Tarea> paginaTareas = new PageImpl<>(Collections.singletonList(tarea));
        when(repositorioTarea.findByUsuarioNombreUsuario("usuarioTest", pageable)).thenReturn(paginaTareas);

        // Act
        Page<Tarea> resultado = servicioTarea.listarTareas("usuarioTest", pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Tarea Test", resultado.getContent().get(0).getTitulo());
    }

    @Test
    void obtenerTarea_exito() {
        // Arrange
        when(repositorioTarea.findById(1L)).thenReturn(Optional.of(tarea));

        // Act
        Tarea resultado = servicioTarea.obtenerTarea(1L, "usuarioTest");

        // Assert
        assertNotNull(resultado);
        assertEquals("Tarea Test", resultado.getTitulo());
    }

    @Test
    void obtenerTarea_noAutorizado_lanzaExcepcion() {
        // Arrange
        usuario.setNombreUsuario("otroUsuario");
        when(repositorioTarea.findById(1L)).thenReturn(Optional.of(tarea));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioTarea.obtenerTarea(1L, "usuarioTest");
        });
        assertEquals("No autorizado para acceder a esta tarea", exception.getMessage());
    }

    @Test
    void obtenerTarea_noEncontrada_lanzaExcepcion() {
        // Arrange
        when(repositorioTarea.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioTarea.obtenerTarea(1L, "usuarioTest");
        });
        assertEquals("Tarea no encontrada", exception.getMessage());
    }

    @Test
    void actualizarTarea_exito() {
        // Arrange
        when(repositorioTarea.findById(1L)).thenReturn(Optional.of(tarea));
        when(repositorioTarea.save(any(Tarea.class))).thenReturn(tarea);

        // Act
        Tarea resultado = servicioTarea.actualizarTarea(1L, solicitudTarea, "usuarioTest");

        // Assert
        assertNotNull(resultado);
        assertEquals("Tarea Test", resultado.getTitulo());
        verify(repositorioTarea).save(any(Tarea.class));
    }

    @Test
    void eliminarTarea_exito() {
        // Arrange
        when(repositorioTarea.findById(1L)).thenReturn(Optional.of(tarea));

        // Act
        servicioTarea.eliminarTarea(1L, "usuarioTest");

        // Assert
        verify(repositorioTarea).delete(tarea);
    }
}