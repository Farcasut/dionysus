package com.afarcasi.dionysus.service.user;

import com.afarcasi.dionysus.mapper.UserMapper;
import com.afarcasi.dionysus.model.dto.user.UserCreateDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
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
            throw new RuntimeException("User with email " + dto.getEmail() + " already exists.");
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

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: User not found.");
        }
        userRepository.deleteById(id);
    }
}
