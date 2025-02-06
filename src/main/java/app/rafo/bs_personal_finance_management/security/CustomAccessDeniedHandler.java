package app.rafo.bs_personal_finance_management.security;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom handler for managing access denied (403 Forbidden) responses.
 * This class is triggered when an authenticated user tries to access a resource
 * they do not have permission for.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    /**
     * Handles AccessDeniedException and returns a structured JSON response.
     * Logs the denied access attempt with the HTTP method and endpoint.
     *
     * @param request the HTTP request that resulted in an access denied error
     * @param response the HTTP response to modify
     * @param accessDeniedException the exception thrown when access is denied
     * @throws IOException if an input/output error occurs while writing the response
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        logger.warn("❌ Acceso denegado: {} - {}", request.getMethod(), request.getRequestURI());

        // Create a standardized API response for forbidden access
        ApiResponse<String> apiResponse = new ApiResponse<>(
                null,
                "Access Denied: You do not have permission to access this resource.",
                HttpServletResponse.SC_FORBIDDEN,
                0
        );

        // Set response headers and write the JSON response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}

