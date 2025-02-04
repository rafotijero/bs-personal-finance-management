package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.UserDTO;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 * Handles HTTP requests related to user operations.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor-based dependency injection.
     * @param userService Service handling user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     * @return a list of users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        ApiResponse<List<User>> response = new ApiResponse<>(
                users,
                "Successfully retrieved users.",
                HttpStatus.OK.value(),
                users.size()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a user by ID.
     * @param id the ID of the user
     * @return the user if found, or a 404 response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            ApiResponse<User> response = new ApiResponse<>(
                    user.get(),
                    "User found.",
                    HttpStatus.OK.value(),
                    1
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<User> response = new ApiResponse<>(
                    null,
                    "User not found.",
                    HttpStatus.NOT_FOUND.value(),
                    0
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Creates a new user.
     * @param userDTO the user to create
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getPassword() == null) {
            ApiResponse<User> response = new ApiResponse<>(
                    null,
                    "Invalid user data.",
                    HttpStatus.BAD_REQUEST.value(),
                    0
            );
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setRole(userDTO.getRole());

        User createdUser = userService.saveUser(user);
        ApiResponse<User> response = new ApiResponse<>(
                createdUser,
                "User successfully created.",
                HttpStatus.CREATED.value(),
                1
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Deletes a user by ID.
     * @param id the ID of the user to delete
     * @return a response indicating success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = new ApiResponse<>(
                null,
                "User successfully deleted.",
                HttpStatus.NO_CONTENT.value(),
                0
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
