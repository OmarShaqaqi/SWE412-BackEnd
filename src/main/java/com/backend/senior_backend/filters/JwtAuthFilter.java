package com.backend.senior_backend.filters;

import com.backend.senior_backend.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
    
        System.out.println("🔍 Incoming Request: " + request.getMethod() + " " + request.getRequestURI());
    
        String phone = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            phone = jwtUtils.extractPhone(jwt);
        }

        if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtils.isTokenInvalidated(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Other authentication logic...
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ No JWT provided. Allowing request...");
            filterChain.doFilter(request, response);
            return;
        }
    
        String token = authHeader.substring(7);
        phone = jwtUtils.extractPhone(token);
    
        System.out.println("🔍 Extracted Phone from Token: " + phone);
    
        if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtils.isTokenValid(token, phone)) {
                System.out.println("✅ Token is valid. Setting authentication for: " + phone);
                UserDetails userDetails = User.withUsername(phone)
                        .password("")
                        .roles("USER")
                        .build();
    
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    
                System.out.println("🔓 Authentication Set Successfully!");
            } else {
                System.out.println("❌ Token is invalid!");
            }
        } else {
            System.out.println("⚠️ Authentication already exists or phone is null.");
        }
    
        filterChain.doFilter(request, response);
    }
    


}
