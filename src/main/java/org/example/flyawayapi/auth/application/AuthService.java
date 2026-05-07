package org.example.flyawayapi.auth.application;

import org.example.flyawayapi.auth.dto.AuthToken;
import org.example.flyawayapi.auth.dto.LoginDTO;
import org.example.flyawayapi.user.domain.User;
import org.example.flyawayapi.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthToken login(LoginDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new RuntimeException("Email is mandatory");
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("Password is mandatory");
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Unknown email"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user);

        return new AuthToken(token);
    }
}