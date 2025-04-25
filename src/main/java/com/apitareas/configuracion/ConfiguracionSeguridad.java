package com.apitareas.configuracion;

import com.apitareas.entidades.Usuario;
import com.apitareas.repositorios.RepositorioUsuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private final FiltroJwt filtroJwt;
    private final RepositorioUsuario repositorioUsuario;

    // Inyección por constructor
    public ConfiguracionSeguridad(FiltroJwt filtroJwt, RepositorioUsuario repositorioUsuario) {
        this.filtroJwt = filtroJwt;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Nueva sintaxis para deshabilitar CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Nueva sintaxis para sessionManagement
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/autenticacion/**", "/swagger-ui/**", "/api-docs/**").permitAll() // Reemplazo de antMatchers por requestMatchers
                        .requestMatchers("/tareas/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration configAutenticacion) throws Exception {
        return configAutenticacion.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario usuario = repositorioUsuario.findByNombreUsuario(username);
            if (usuario == null) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + username);
            }
            return org.springframework.security.core.userdetails.User
                    .withUsername(usuario.getNombreUsuario())
                    .password(usuario.getContrasena())
                    .roles(usuario.getRol())
                    .build();
        };
    }
}