package com.afarcasi.dionysus.controller.user;

import com.afarcasi.dionysus.exception.InvalidCredentialsException;
import com.afarcasi.dionysus.exception.UserEmailAlreadyExistsException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.model.dto.user.UserCreateDTO;
import com.afarcasi.dionysus.model.dto.user.UserLoginDTO;
import com.afarcasi.dionysus.model.dto.user.UserPasswordUpdateDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import com.afarcasi.dionysus.model.dto.user.UserUpdateDTO;
import com.afarcasi.dionysus.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user handling.")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user with a specific role")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody @NotNull UserCreateDTO dto) {
        try {
            return new ResponseEntity<>(userService.registerUser(dto), HttpStatus.CREATED);
        } catch (UserEmailAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody @NotNull UserLoginDTO dto) {
        try {
            UserResponseDTO user = userService.login(dto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details by their ID")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        Optional<UserResponseDTO> user = userService.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user details(firstName, lastName, email)")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody @NotNull UserUpdateDTO dto) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(id, dto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserEmailAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Update user password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody @NotNull UserPasswordUpdateDTO dto) {
        try {
            userService.updatePassword(id, dto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user account")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
