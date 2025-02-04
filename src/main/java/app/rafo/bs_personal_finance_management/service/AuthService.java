package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.auth.AuthenticationRequest;
import app.rafo.bs_personal_finance_management.auth.AuthenticationResponse;
import app.rafo.bs_personal_finance_management.auth.RegisterRequest;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import app.rafo.bs_personal_finance_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling authentication logic, including user registration and login.
 * This service interacts with the database to manage user authentication and
 * generates JWT tokens for authenticated users.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository; // Repository for user data access
    private final JwtService jwtService; // Service for JWT token generation and validation
    private final AuthenticationManager authenticationManager; // Manages user authentication
    private final PasswordEncoder passwordEncoder; // Encrypts passwords for security

    /**
     * Authenticates a user and generates a JWT token.
     * This method verifies user credentials and, if valid, returns an authentication token.
     *
     * @param request Authentication request containing email and password.
     * @return AuthenticationResponse containing the JWT token.
     * @throws RuntimeException if the user is not found or credentials are invalid.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate user credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Retrieve user details from the database
        UserDetails user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token using the user's email as identifier
        String jwtToken = jwtService.generateToken(user.getUsername());

        return new AuthenticationResponse(jwtToken);
    }

    /**
     * Registers a new user in the system and generates a JWT token.
     * This method checks if the email is already in use, encrypts the password,
     * saves the new user to the database, and returns an authentication token.
     *
     * @param request The registration request containing user details.
     * @return AuthenticationResponse containing a JWT token or an error message.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if the email is already registered
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthenticationResponse("ERROR: Email already registered.");
        }

        // Create a new user entity and set its properties
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        newUser.setName(request.getName());
        newUser.setRole(request.getRole());

        // Save the user to the database
        userRepository.save(newUser);

        // Generate JWT token using the user's email
        String jwtToken = jwtService.generateToken(newUser.getEmail());

        return new AuthenticationResponse(jwtToken);
    }
}
