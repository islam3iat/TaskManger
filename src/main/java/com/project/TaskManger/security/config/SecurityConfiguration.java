package com.project.TaskManger.security.config;

import com.project.TaskManger.security.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.project.TaskManger.security.user.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request->request
                .requestMatchers("/api/v1/auth/**")
                .permitAll()
                .requestMatchers("/").permitAll().requestMatchers("http://localhost:8080").permitAll().anyRequest().permitAll())
//            .requestMatchers("api/v1/category/**").hasRole(ADMIN.name())
//              .requestMatchers(GET, "api/v1/category/**").hasAuthority(String.valueOf(Role.ADMIN))
//            .requestMatchers(POST, "api/v1/category/**").hasAuthority(String.valueOf(Role.ADMIN))
//            .requestMatchers(PUT, "api/v1/category/**").hasAuthority(String.valueOf(Role.ADMIN))
//            .requestMatchers(DELETE, "api/v1/category/**").hasAuthority(String.valueOf(Role.ADMIN))
        .sessionManagement(session->session.
                sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
