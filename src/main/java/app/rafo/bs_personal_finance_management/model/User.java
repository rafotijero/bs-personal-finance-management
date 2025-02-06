package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * Entity representing a user in the system.
 * Each user has a unique email and is assigned a role (ADMIN or USER).
 * This entity implements the UserDetails interface to integrate with Spring Security.
 */
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User implements UserDetails {

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

    /**
     * Returns the authorities granted to the user. Each user is assigned a role
     * which is converted into a granted authority for Spring Security.
     *
     * @return A collection containing the granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Returns the username used to authenticate the user.
     * In this case, the user's email is used as the username.
     *
     * @return The email of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account is expired.
     * Returning {@code true} means the account is valid (not expired).
     *
     * @return {@code true} if the account is not expired, {@code false} otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * Returning {@code true} means the account is not locked.
     *
     * @return {@code true} if the account is not locked, {@code false} otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) are expired.
     * Returning {@code true} means the credentials are valid (not expired).
     *
     * @return {@code true} if the credentials are not expired, {@code false} otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * Returning {@code true} means the user is enabled.
     *
     * @return {@code true} if the user is enabled, {@code false} otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
