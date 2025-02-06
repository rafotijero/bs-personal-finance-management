package app.rafo.bs_personal_finance_management.config;

import app.rafo.bs_personal_finance_management.repository.UserRepository;
import app.rafo.bs_personal_finance_management.security.CustomAccessDeniedHandler;
import app.rafo.bs_personal_finance_management.security.CustomAuthenticationEntryPoint;
import app.rafo.bs_personal_finance_management.security.JwtAuthenticationFilter;
import app.rafo.bs_personal_finance_management.service.impl.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for managing HTTP security settings.
 * This class defines authentication, authorization, and security mechanisms
 * for the application, integrating a custom UserDetailsService for authentication.
 */
@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    /**
     * Constructor for injecting required security components.
     *
     * @param userDetailsService Implementation for loading user-specific data.
     * @param jwtAuthenticationFilter JWT filter for authentication.
     * @param authenticationEntryPoint Handler for unauthorized access (401).
     * @param accessDeniedHandler Handler for forbidden access (403).
     */
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Configures security filter chain.
     * - Disables CSRF (for testing purposes).
     * - Allows unauthenticated access to `/auth/**` endpoints.
     * - Restricts `/users/**` to ADMIN roles.
     * - Requires authentication for all other requests.
     * - Configures JWT filter and exception handling.
     *
     * @param http HttpSecurity instance.
     * @return Configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("✅ Configuring SecurityFilterChain...");

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/banks/**").hasRole("ADMIN")  // 🔹 Solo ADMIN puede hacer POST
                        .requestMatchers(HttpMethod.GET, "/banks/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides an AuthenticationManager bean for authentication handling.
     *
     * @param authenticationConfiguration Authentication configuration object.
     * @return Configured AuthenticationManager.
     * @throws Exception If an error occurs.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder bean for password encryption.
     * Uses BCrypt algorithm for security.
     *
     * @return PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides UserDetailsService bean implementation.
     *
     * @param userRepository Repository to fetch user details.
     * @return Configured UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }
}