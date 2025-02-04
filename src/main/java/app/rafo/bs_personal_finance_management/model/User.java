package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a user in the system.
 * Each user has a unique email and is assigned a role (ADMIN or USER).
 */
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the user

    @Column(unique = true, nullable = false)
    private String email; // User's email (must be unique)

    @Column(nullable = false)
    private String password; // Encrypted password

    private String name; // Full name of the user

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // Defines if the user is an ADMIN or a regular USER
}
