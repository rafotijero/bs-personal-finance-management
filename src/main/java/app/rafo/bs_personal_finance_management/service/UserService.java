package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for handling user-related operations.
 * This service provides methods for retrieving, saving, and deleting users from the database.
 */
@Service
public class UserService {

    private final UserRepository userRepository; // Repository for accessing user data

    /**
     * Constructor-based dependency injection.
     *
     * @param userRepository The repository responsible for database operations related to users.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all registered users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their unique identifier.
     *
     * @param id The ID of the user to retrieve.
     * @return An {@link Optional} containing the user if found, otherwise empty.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Saves a new user or updates an existing one in the database.
     *
     * @param user The user entity to save.
     * @return The saved user object.
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user from the database based on their ID.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}