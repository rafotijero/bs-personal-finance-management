package app.rafo.bs_personal_finance_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for managing HTTP security settings.
 * This class defines how authentication, authorization, and other security mechanisms
 * are configured for the application. It integrates a custom UserDetailsService
 * to handle user details during authentication.
 */
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Constructor for injecting a custom UserDetailsService implementation.
     *
     * @param userDetailsService The UserDetailsService implementation used to load user-specific data.
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain for the application.
     * - Disables CSRF protection for testing purposes.
     * - Allows unauthenticated access to authentication-related endpoints under `/auth/**`.
     * - Restricts access to `/users/**` for users with the "ADMIN" role only.
     * - Requires authentication for all other requests.
     * - Associates the custom UserDetailsService for user authentication.
     * - Disables anonymous authentication.
     *
     * @param http The HttpSecurity object used to configure security.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disables CSRF protection for testing purposes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Allows access to authentication endpoints
                        .requestMatchers("/users/**").hasRole("ADMIN") // Restricts access to "ADMIN" role
                        .anyRequest().authenticated() // Requires authentication for other endpoints
                )
                .userDetailsService(userDetailsService) // Configures UserDetailsService
                .anonymous(anonymous -> anonymous.disable()) // Disables anonymous authentication
                .build();
    }

    /**
     * Provides an AuthenticationManager bean, which is used to authenticate users.
     *
     * @param authenticationConfiguration The AuthenticationConfiguration object for setting up authentication.
     * @return The configured AuthenticationManager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder bean for encoding and validating passwords.
     * This implementation uses BCrypt for strong password encryption.
     *
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



