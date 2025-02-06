package app.rafo.bs_personal_finance_management.exception;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for managing security-related exceptions.
 * This class provides centralized handling of access denied and authentication exceptions
 * to return consistent API responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AccessDeniedException when a user tries to access a restricted resource.
     * Returns a 403 Forbidden response with a descriptive message.
     *
     * @param ex the AccessDeniedException thrown by Spring Security
     * @return ResponseEntity<ApiResponse<String>> containing the error message and status code
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                null,
                "Access denied: " + ex.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                0
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Handles AuthenticationException when authentication fails (e.g., invalid credentials).
     * Returns a 401 Unauthorized response with a descriptive message.
     *
     * @param ex the AuthenticationException thrown during authentication failure
     * @return ResponseEntity<ApiResponse<String>> containing the error message and status code
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                null,
                "Authentication failed: " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                0
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
