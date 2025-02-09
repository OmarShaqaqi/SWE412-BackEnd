package com.backend.senior_backend.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.service.CustomUserDetailsService;
import com.backend.senior_backend.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{


    private  JWTService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JWTService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                

                String authHeader = request.getHeader("Authorization");
                String token = null;
                String phone = null;

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    phone = jwtService.extractPhone(token);
                }

                if(phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Users userDetails =  (Users) userDetailsService.loadUserByUsername(phone);
                    

                    if(jwtService.isTokenInvalidated(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(phone, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                }
        }
        filterChain.doFilter(request, response);

    }
}
