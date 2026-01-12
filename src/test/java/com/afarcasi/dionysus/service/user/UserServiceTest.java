package com.afarcasi.dionysus.service.user;

import com.afarcasi.dionysus.exception.InvalidCredentialsException;
import com.afarcasi.dionysus.exception.UserEmailAlreadyExistsException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.mapper.UserMapper;
import com.afarcasi.dionysus.model.dto.user.UserCreateDTO;
import com.afarcasi.dionysus.model.dto.user.UserLoginDTO;
import com.afarcasi.dionysus.model.dto.user.UserPasswordUpdateDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import com.afarcasi.dionysus.model.dto.user.UserUpdateDTO;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponseDTO userResponseDTO;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;
    private UserPasswordUpdateDTO userPasswordUpdateDTO;
    private UserLoginDTO userLoginDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPasswordHash("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(UserCategory.NORMAL_USER);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setEmail("test@example.com");
        userResponseDTO.setUsername("testuser");
        userResponseDTO.setFirstName("John");
        userResponseDTO.setLastName("Doe");
        userResponseDTO.setRole(UserCategory.NORMAL_USER);

        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setEmail("new@example.com");
        userCreateDTO.setUsername("newuser");
        userCreateDTO.setPassword("newpassword");
        userCreateDTO.setFirstName("Jane");
        userCreateDTO.setLastName("Smith");
        userCreateDTO.setRole(UserCategory.NORMAL_USER);

        userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setEmail("updated@example.com");
        userUpdateDTO.setFirstName("Updated");
        userUpdateDTO.setLastName("Name");

        userPasswordUpdateDTO = new UserPasswordUpdateDTO();
        userPasswordUpdateDTO.setNewPassword("newpassword123");

        userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername("testuser");
        userLoginDTO.setPassword("password123");
    }

    @Test
    void registerUser_WhenEmailDoesNotExist_ShouldReturnUserResponseDTO() {
        when(userRepository.findByEmail(userCreateDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.registerUser(userCreateDTO);

        assertNotNull(result);
        assertEquals(userResponseDTO.getId(), result.getId());
        assertEquals(userResponseDTO.getEmail(), result.getEmail());
        verify(userRepository).findByEmail(userCreateDTO.getEmail());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toResponseDTO(any(User.class));
    }

    @Test
    void registerUser_WhenEmailAlreadyExists_ShouldThrowUserEmailAlreadyExistsException() {
        User existingUser = new User();
        when(userRepository.findByEmail(userCreateDTO.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(UserEmailAlreadyExistsException.class, () -> userService.registerUser(userCreateDTO));
        verify(userRepository).findByEmail(userCreateDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUserResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponseDTO);

        Optional<UserResponseDTO> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(userResponseDTO.getId(), result.get().getId());
        verify(userRepository).findById(1L);
        verify(userMapper).toResponseDTO(user);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.findById(1L);

        assertTrue(result.isEmpty());
        verify(userRepository).findById(1L);
        verify(userMapper, never()).toResponseDTO(any(User.class));
    }

    @Test
    void login_WhenCredentialsAreValid_ShouldReturnUserResponseDTO() {
        when(userRepository.findByUsername(userLoginDTO.getUsername())).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.login(userLoginDTO);

        assertNotNull(result);
        assertEquals(userResponseDTO.getId(), result.getId());
        verify(userRepository).findByUsername(userLoginDTO.getUsername());
        verify(userMapper).toResponseDTO(user);
    }

    @Test
    void login_WhenUserDoesNotExist_ShouldThrowInvalidCredentialsException() {
        when(userRepository.findByUsername(userLoginDTO.getUsername())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> userService.login(userLoginDTO));
        verify(userRepository).findByUsername(userLoginDTO.getUsername());
        verify(userMapper, never()).toResponseDTO(any(User.class));
    }

    @Test
    void login_WhenPasswordIsIncorrect_ShouldThrowInvalidCredentialsException() {
        userLoginDTO.setPassword("wrongpassword");
        when(userRepository.findByUsername(userLoginDTO.getUsername())).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> userService.login(userLoginDTO));
        verify(userRepository).findByUsername(userLoginDTO.getUsername());
        verify(userMapper, never()).toResponseDTO(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUserResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userUpdateDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateUser(1L, userUpdateDTO);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).findByEmail(userUpdateDTO.getEmail());
        verify(userMapper).updateEntity(userUpdateDTO, user);
        verify(userRepository).save(user);
        verify(userMapper).toResponseDTO(user);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, userUpdateDTO));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenEmailIsAlreadyTaken_ShouldThrowUserEmailAlreadyExistsException() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail(userUpdateDTO.getEmail());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userUpdateDTO.getEmail())).thenReturn(Optional.of(anotherUser));

        assertThrows(UserEmailAlreadyExistsException.class, () -> userService.updateUser(1L, userUpdateDTO));
        verify(userRepository).findById(1L);
        verify(userRepository).findByEmail(userUpdateDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenEmailIsSameAsCurrent_ShouldNotThrowException() {
        userUpdateDTO.setEmail(user.getEmail());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateUser(1L, userUpdateDTO);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userMapper).updateEntity(userUpdateDTO, user);
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_WhenUserExists_ShouldUpdatePassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updatePassword(1L, userPasswordUpdateDTO);

        assertEquals(userPasswordUpdateDTO.getNewPassword(), user.getPasswordHash());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(1L, userPasswordUpdateDTO));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
