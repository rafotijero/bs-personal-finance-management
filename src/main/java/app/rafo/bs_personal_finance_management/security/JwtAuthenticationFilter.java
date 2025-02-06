package app.rafo.bs_personal_finance_management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Authentication Filter for processing authentication requests.
 * This filter intercepts incoming HTTP requests, extracts JWT tokens from the
 * Authorization header, validates them, and sets the authentication in the SecurityContext.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for dependency injection.
     *
     * @param jwtService the service for handling JWT operations (generation, validation, extraction)
     * @param userDetailsService the service for loading user details
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Intercepts each HTTP request to check for a valid JWT token.
     * If a valid token is found, it extracts the user details and sets the authentication.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to pass the request along if authentication is successful
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an input-output error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Verificar si el encabezado de autorización es válido
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("🚨 No JWT token found in Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token JWT
        String token = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(token);

        // Verificar si el email se extrajo correctamente y si no hay autenticación previa
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("🔑 Extracted email from JWT: " + userEmail);

            // Cargar los detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            logger.info("🔑 Loaded user details: " + userDetails.getUsername());

            // Extraer los roles del token JWT
            List<GrantedAuthority> authorities = jwtService.extractRoles(token);
            logger.info("🔑 Extracted roles from JWT: " + authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            // Validar el token JWT
            if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                logger.info("✅ JWT token is valid");

                // Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecer la autenticación en el SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("🔑 Authentication set in SecurityContext for user: " + userDetails.getUsername());
            } else {
                logger.warn("🚨 JWT token is invalid");
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

}
