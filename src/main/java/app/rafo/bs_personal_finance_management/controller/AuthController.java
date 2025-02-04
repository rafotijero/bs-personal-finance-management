package app.rafo.bs_personal_finance_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller to handle authentication-related endpoints.
 * Provides public endpoints that do not require authentication.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * A public test endpoint that does not require authentication.
     *
     * @return A ResponseEntity containing a success message.
     */
    @GetMapping("/test")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This endpoint is public and does not require authentication!");
    }
}
