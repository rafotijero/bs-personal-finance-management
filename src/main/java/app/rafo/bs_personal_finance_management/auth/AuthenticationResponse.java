package app.rafo.bs_personal_finance_management.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the authentication response containing the generated JWT token.
 */
@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}
