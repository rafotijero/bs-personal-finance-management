package app.rafo.bs_personal_finance_management.auth;

import app.rafo.bs_personal_finance_management.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user registration request.
 * Contains user details required for registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private UserRole role;
}