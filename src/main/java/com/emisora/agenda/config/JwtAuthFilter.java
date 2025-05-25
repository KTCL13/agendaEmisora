package com.emisora.agenda.config;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component // Es importante que este filtro sea un Bean de Spring para que se aplique
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider; 

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Authentication authentication = userAuthProvider.validateTokenAndGetAuthentication(jwt);

                
                if (authentication instanceof org.springframework.security.authentication.AbstractAuthenticationToken) {
                    ((org.springframework.security.authentication.AbstractAuthenticationToken) authentication)
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                }


                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Autenticación establecida en el contexto de seguridad para el token.");

            } catch (JWTVerificationException e) {

                logger.warn("Error de validación del token JWT: " + e.getMessage());
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authentication Error: " + e.getMessage());
                 return;
            } catch (Exception e) {
                logger.error("No se pudo establecer la autenticación del usuario en el contexto de seguridad.", e);
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
