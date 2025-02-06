package app.rafo.bs_personal_finance_management.service.impl;

import app.rafo.bs_personal_finance_management.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} for user authentication.
 * This service is responsible for loading user details from the database
 * based on the provided email address.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository the repository for accessing user data
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email for authentication.
     * This method is used by Spring Security during authentication processes.
     *
     * @param email the email of the user attempting to authenticate
     * @return a {@link UserDetails} object containing the user's credentials and roles
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> User.builder()
                        .username(user.getEmail())  // Set email as username
                        .password(user.getPassword()) // Set the encrypted password
                        .roles(user.getRole().name()) // Assign roles (e.g., ADMIN, USER)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
