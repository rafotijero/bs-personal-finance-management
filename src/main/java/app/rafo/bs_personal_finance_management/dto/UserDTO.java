package app.rafo.bs_personal_finance_management.dto;

import app.rafo.bs_personal_finance_management.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for transferring user data between client and backend.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String password;
    private String name;
    private UserRole role;
}