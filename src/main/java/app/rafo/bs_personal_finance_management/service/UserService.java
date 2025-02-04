package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for handling user-related operations.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection.
     * @param userRepository User repository instance
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email for Spring Security authentication.
     * @param email the email of the user
     * @return UserDetails required by Spring Security
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Retrieves all users from the database.
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     * @param id the ID of the user
     * @return an Optional containing the user if found, otherwise empty
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Saves a new user or updates an existing one.
     * @param user the user object to save
     * @return the saved user
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by ID.
     * @param id the ID of the user to delete
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}