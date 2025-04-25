package com.apitareas.servicios;

import com.apitareas.entidades.Usuario;
import com.apitareas.repositorios.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioUsuarioTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @Mock
    private PasswordEncoder codificadorContrasena;

    @InjectMocks
    private ServicioUsuario servicioUsuario;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        usuario.setContrasena("password");
        usuario.setCorreo("test@ejemplo.com");
        usuario.setRol("USER");
    }

    @Test
    void registrarUsuario_exito() {
        // Arrange
        when(repositorioUsuario.findByNombreUsuario("usuarioTest")).thenReturn(null);
        when(codificadorContrasena.encode("password")).thenReturn("hashedPassword");
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        servicioUsuario.registrarUsuario(usuario);

        // Assert
        verify(codificadorContrasena).encode("password");
        verify(repositorioUsuario).save(any(Usuario.class));
        assertEquals("hashedPassword", usuario.getContrasena());
    }

    @Test
    void registrarUsuario_nombreUsuarioYaExiste_lanzaExcepcion() {
        // Arrange
        when(repositorioUsuario.findByNombreUsuario("usuarioTest")).thenReturn(usuario);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioUsuario.registrarUsuario(usuario);
        });
        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        verify(repositorioUsuario, never()).save(any(Usuario.class));
    }

    @Test
    void buscarPorNombreUsuario_exito() {
        // Arrange
        when(repositorioUsuario.findByNombreUsuario("usuarioTest")).thenReturn(usuario);

        // Act
        Usuario resultado = servicioUsuario.buscarPorNombreUsuario("usuarioTest");

        // Assert
        assertNotNull(resultado);
        assertEquals("usuarioTest", resultado.getNombreUsuario());
    }

    @Test
    void buscarPorNombreUsuario_noEncontrado_devuelveNull() {
        // Arrange
        when(repositorioUsuario.findByNombreUsuario("usuarioTest")).thenReturn(null);

        // Act
        Usuario resultado = servicioUsuario.buscarPorNombreUsuario("usuarioTest");

        // Assert
        assertNull(resultado);
    }
}