package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.auth.AuthenticationRequest;
import app.rafo.bs_personal_finance_management.auth.AuthenticationResponse;
import app.rafo.bs_personal_finance_management.auth.RegisterRequest;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import app.rafo.bs_personal_finance_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling authentication and user registration.
 * This service manages user login, password validation, and JWT token generation.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository; // Repository for accessing user data
    private final JwtService jwtService; // Service for JWT token generation and validation
    private final AuthenticationManager authenticationManager; // Manages authentication process
    private final PasswordEncoder passwordEncoder; // Encrypts passwords for security
    private final UserDetailsService userDetailsService; // Loads user details for authentication

    /**
     * Authenticates a user and generates a JWT token.
     * This method validates the provided credentials and returns an authentication token.
     *
     * @param request The authentication request containing email and password.
     * @return {@link AuthenticationResponse} containing the generated JWT token.
     * @throws RuntimeException if the credentials are invalid.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Retrieve user details from the database
        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        // Verify the password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT token using the user's email
        String jwtToken = jwtService.generateToken(user.getUsername());

        return new AuthenticationResponse(jwtToken);
    }

    /**
     * Registers a new user in the system and generates a JWT token.
     * This method ensures the email is not already registered, encrypts the password,
     * stores the new user in the database, and returns an authentication token.
     *
     * @param request The registration request containing user details.
     * @return {@link AuthenticationResponse} containing the generated JWT token or an error message.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if the email is already registered
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthenticationResponse("ERROR: Email already registered.");
        }

        // Create and populate a new user entity
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        newUser.setName(request.getName());
        newUser.setRole(request.getRole());

        // Save the user in the database
        userRepository.save(newUser);

        // Generate JWT token using the user's email
        String jwtToken = jwtService.generateToken(newUser.getEmail());

        return new AuthenticationResponse(jwtToken);
    }
}