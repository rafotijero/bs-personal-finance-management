package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.auth.AuthenticationResponse;
import app.rafo.bs_personal_finance_management.auth.AuthenticationRequest;
import app.rafo.bs_personal_finance_management.auth.RegisterRequest;
import app.rafo.bs_personal_finance_management.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller to handle authentication-related endpoints.
 * Provides login and registration functionality.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor-based dependency injection.
     * @param authService Service handling authentication logic.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Public test endpoint that does not require authentication.
     *
     * @return A ResponseEntity containing a success message.
     */
    @GetMapping("/test")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This endpoint is public and does not require authentication!");
    }

    /**
     * Endpoint to register a new user.
     *
     * @param request The registration request containing user details.
     * @return A ResponseEntity with the authentication response.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse authResponse = authService.register(request);
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(
                authResponse,
                "User successfully registered.",
                HttpStatus.CREATED.value(),
                1
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint to authenticate a user.
     *
     * @param request The authentication request containing email and password.
     * @return A ResponseEntity with the authentication response.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authResponse = authService.authenticate(request);
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(
                authResponse,
                "User successfully authenticated.",
                HttpStatus.OK.value(),
                1
        );
        return ResponseEntity.ok(response);
    }
}
