package org.example.flyawayapi.auth.infrastructure;

import org.example.flyawayapi.auth.application.AuthService;
import org.example.flyawayapi.auth.dto.AuthToken;
import org.example.flyawayapi.auth.dto.LoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody LoginDTO login) {
        return ResponseEntity.ok(authService.login(login));
    }
}