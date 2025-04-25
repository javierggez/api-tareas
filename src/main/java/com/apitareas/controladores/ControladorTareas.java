package com.apitareas.controladores;

import com.apitareas.dto.SolicitudTarea;
import com.apitareas.entidades.Tarea;
import com.apitareas.servicios.ServicioTarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tareas")
public class ControladorTareas {

    @Autowired
    private ServicioTarea servicioTarea;

    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@Valid @RequestBody SolicitudTarea solicitud) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Tarea tarea = servicioTarea.crearTarea(solicitud, nombreUsuario);
        return ResponseEntity.ok(tarea);
    }

    @GetMapping
    public ResponseEntity<Page<Tarea>> listarTareas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano,
            @RequestParam(defaultValue = "fechaCreacion") String ordenarPor,
            @RequestParam(defaultValue = "desc") String direccion) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort.Direction dir = direccion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Tarea> tareas = servicioTarea.listarTareas(nombreUsuario, PageRequest.of(pagina, tamano, Sort.by(dir, ordenarPor)));
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerTarea(@PathVariable Long id) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Tarea tarea = servicioTarea.obtenerTarea(id, nombreUsuario);
        return ResponseEntity.ok(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizarTarea(@PathVariable Long id, @Valid @RequestBody SolicitudTarea solicitud) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Tarea tarea = servicioTarea.actualizarTarea(id, solicitud, nombreUsuario);
        return ResponseEntity.ok(tarea);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTarea(@PathVariable Long id) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        servicioTarea.eliminarTarea(id, nombreUsuario);
        return ResponseEntity.ok("Tarea eliminada con éxito");
    }
}