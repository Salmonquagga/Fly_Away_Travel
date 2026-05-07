package org.example.flyawayapi.user.application;

import org.example.flyawayapi.flight.dto.NewIdDTO;
import org.example.flyawayapi.user.domain.User;
import org.example.flyawayapi.user.dto.RegisterUserDTO;
import org.example.flyawayapi.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(RegisterUserDTO dto) {

        // First name obligatorio
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new RuntimeException("First name is mandatory");
        }

        // Last name obligatorio
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new RuntimeException("Last name is mandatory");
        }

        // Debe tener al menos una mayúscula
        if (!dto.getFirstName().matches(".*[A-Z].*")) {
            throw new RuntimeException("First name must contain uppercase letter");
        }

        if (!dto.getLastName().matches(".*[A-Z].*")) {
            throw new RuntimeException("Last name must contain uppercase letter");
        }

        // Email obligatorio
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new RuntimeException("Email is mandatory");
        }

        // Validación email
        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Invalid email");
        }

        // Password obligatoria
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("Password is mandatory");
        }

        // Password mínimo 8 caracteres, 1 letra y 1 número
        if (!dto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {
            throw new RuntimeException("Invalid password");
        }

        // Email único
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return userRepository.save(user);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}