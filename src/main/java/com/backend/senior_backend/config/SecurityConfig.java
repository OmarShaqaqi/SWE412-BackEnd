package com.backend.senior_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.backend.senior_backend.filters.JwtAuthFilter;
import com.backend.senior_backend.utils.JwtUtils;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())  // Disable CSRF for testing
    //         .authorizeHttpRequests(auth -> auth
    //             .anyRequest().permitAll()  // 🚨 Allow all requests (Temporary for debugging)
    //         )
    //         .formLogin(form -> form.disable())  
    //         .httpBasic(httpBasic -> httpBasic.disable()); 

    //     return http.build();
private final JwtUtils jwtUtils;

public SecurityConfig(JwtUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/signup","/auth/login", "/hello").permitAll()  // Allow login & signup
            .anyRequest().authenticated()  // Protect other endpoints
        )
        .addFilterBefore(new JwtAuthFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
        .formLogin(form -> form.disable())  
        .httpBasic(httpBasic -> httpBasic.disable()); 

    return http.build();
}

}
