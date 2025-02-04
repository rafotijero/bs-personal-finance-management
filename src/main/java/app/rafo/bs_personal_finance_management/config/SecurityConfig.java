package app.rafo.bs_personal_finance_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for managing HTTP security settings.
 * This configuration handles request authorization and disables CSRF protection.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines the security filter chain that specifies authentication rules.
     *
     * @param http The HttpSecurity object for configuring security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // 🔹 Disables CSRF protection for testing purposes.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // 🔹 Allows access to authentication-related endpoints.
                        .anyRequest().authenticated() // 🔹 Requires authentication for all other requests.
                )
                .anonymous(anonymous -> anonymous.disable()) // 🔹 Disables anonymous authentication.
                .build();
    }
}



