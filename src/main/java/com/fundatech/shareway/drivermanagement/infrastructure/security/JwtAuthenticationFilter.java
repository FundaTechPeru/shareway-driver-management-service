package com.fundatech.shareway.drivermanagement.infrastructure.security;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authenticates requests carrying a valid bearer token. The user is reloaded from the database on every
 * request, so role changes (for example PASSENGER to DRIVER) apply without issuing a new token.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final WebAuthenticationDetailsSource detailsSource = new WebAuthenticationDetailsSource();

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            jwtService.extractSubject(header.substring(BEARER_PREFIX.length()).trim())
                    .flatMap(this::loadUser)
                    .ifPresent(user -> authenticate(user, request));
        }
        filterChain.doFilter(request, response);
    }

    private Optional<UserDetails> loadUser(String email) {
        try {
            return Optional.of(userDetailsService.loadUserByUsername(email));
        } catch (UsernameNotFoundException ex) {
            return Optional.empty();
        }
    }

    private void authenticate(UserDetails user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
        authentication.setDetails(detailsSource.buildDetails(request));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
