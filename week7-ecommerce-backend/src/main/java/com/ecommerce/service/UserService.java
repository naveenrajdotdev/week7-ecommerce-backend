package com.ecommerce.service;

import com.ecommerce.model.dto.UserDTO;
import com.ecommerce.model.entity.User;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO register(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email already registered");
        User user = User.builder()
                .name(dto.getName()).email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole()).build();
        return toDTO(userRepository.save(user));
    }

    public UserDTO getById(Long id) {
        return userRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void delete(Long id) { userRepository.deleteById(id); }

    private UserDTO toDTO(User u) {
        return UserDTO.builder().id(u.getId()).name(u.getName())
                .email(u.getEmail()).role(u.getRole()).build();
    }
}