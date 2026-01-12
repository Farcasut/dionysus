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
import com.afarcasi.dionysus.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO registerUser(UserCreateDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserEmailAlreadyExistsException(dto.getEmail());
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());
        user.setRole(dto.getRole());
        user.setUsername(dto.getUsername());
        userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    public Optional<UserResponseDTO> findById(Long id) {
       Optional<User> user = userRepository.findById(id);
       if (user.isPresent()) {
           return Optional.of(userMapper.toResponseDTO(user.get()));
       }
       return Optional.empty();
    }

    public UserResponseDTO login(UserLoginDTO dto) {
        Optional<User> userOptional = userRepository.findByUsername(dto.getUsername());
        
        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        
        User user = userOptional.get();
        
        if (!user.getPasswordHash().equals(dto.getPassword())) {
            throw new InvalidCredentialsException();
        }
        
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        // Check if email is being updated and if it's already taken by another user
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new UserEmailAlreadyExistsException(dto.getEmail());
            }
        }
        
        userMapper.updateEntity(dto, user);
        userRepository.save(user);
        
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public void updatePassword(Long id, UserPasswordUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        user.setPasswordHash(dto.getNewPassword());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
