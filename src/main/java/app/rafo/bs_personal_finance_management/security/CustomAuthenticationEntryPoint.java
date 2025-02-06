package app.rafo.bs_personal_finance_management.security;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom authentication entry point for handling unauthorized access (401 Unauthorized).
 * This class is triggered when an unauthenticated user attempts to access a secured resource.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    /**
     * Handles authentication failures and returns a structured JSON response.
     * Logs authentication errors when a request is denied due to missing or invalid credentials.
     *
     * @param request the HTTP request that resulted in an authentication failure
     * @param response the HTTP response to modify
     * @param authException the exception thrown when authentication fails
     * @throws IOException if an input/output error occurs while writing the response
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        logger.error("🚨 Authentication error: {}", authException.getMessage());

        // Create a standardized API response for unauthorized access
        ApiResponse<String> apiResponse = new ApiResponse<>(
                null,
                "Authentication failed: " + authException.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                0
        );

        // Set response headers and write the JSON response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}
