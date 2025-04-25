package com.apitareas.servicios;

import com.apitareas.entidades.Usuario;
import com.apitareas.repositorios.RepositorioUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder codificadorContrasena;

    public ServicioUsuario(RepositorioUsuario repositorioUsuario, PasswordEncoder codificadorContrasena) {
        this.repositorioUsuario = repositorioUsuario;
        this.codificadorContrasena = codificadorContrasena;
    }

    public void registrarUsuario(Usuario usuario) {
        // Validar si el nombre de usuario ya existe
        if (repositorioUsuario.findByNombreUsuario(usuario.getNombreUsuario()) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        // Validar si el correo ya existe
        if (repositorioUsuario.findByCorreo(usuario.getCorreo()) != null) {
            throw new IllegalArgumentException("El correo ya está en uso");
        }
        // Cifrar la contraseña
        usuario.setContrasena(codificadorContrasena.encode(usuario.getContrasena()));
        repositorioUsuario.save(usuario);
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return repositorioUsuario.findByNombreUsuario(nombreUsuario);
    }

    public Usuario buscarPorCorreo(String correo) {
        return repositorioUsuario.findByCorreo(correo);
    }
}