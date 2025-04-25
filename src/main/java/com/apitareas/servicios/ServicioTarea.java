package com.apitareas.servicios;

import com.apitareas.dto.SolicitudTarea;
import com.apitareas.entidades.Tarea;
import com.apitareas.entidades.Usuario;
import com.apitareas.repositorios.RepositorioTarea;
import com.apitareas.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServicioTarea {

    @Autowired
    private RepositorioTarea repositorioTarea;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public Tarea crearTarea(SolicitudTarea solicitud, String nombreUsuario) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(solicitud.getTitulo());
        tarea.setDescripcion(solicitud.getDescripcion());
        tarea.setCompletada(solicitud.isCompletada());
        tarea.setUsuario(repositorioUsuario.findByNombreUsuario(nombreUsuario));
        return repositorioTarea.save(tarea);
    }

    public Page<Tarea> listarTareas(String nombreUsuario, Pageable pageable) {
        return repositorioTarea.findByUsuarioNombreUsuario(nombreUsuario, pageable);
    }

    public Tarea obtenerTarea(Long id, String nombreUsuario) {
        Tarea tarea = repositorioTarea.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        if (!tarea.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
            throw new RuntimeException("No autorizado para acceder a esta tarea");
        }
        return tarea;
    }

    public Tarea actualizarTarea(Long id, SolicitudTarea solicitud, String nombreUsuario) {
        Tarea tarea = obtenerTarea(id, nombreUsuario);
        tarea.setTitulo(solicitud.getTitulo());
        tarea.setDescripcion(solicitud.getDescripcion());
        tarea.setCompletada(solicitud.isCompletada());
        return repositorioTarea.save(tarea);
    }

    public void eliminarTarea(Long id, String nombreUsuario) {
        Tarea tarea = obtenerTarea(id, nombreUsuario);
        repositorioTarea.delete(tarea);
    }
}