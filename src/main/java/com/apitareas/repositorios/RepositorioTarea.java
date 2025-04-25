package com.apitareas.repositorios;

import com.apitareas.entidades.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioTarea extends JpaRepository<Tarea, Long> {
    Page<Tarea> findByUsuarioNombreUsuario(String nombreUsuario, Pageable pageable);
}