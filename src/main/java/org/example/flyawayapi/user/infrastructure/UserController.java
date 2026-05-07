package org.example.flyawayapi.user.infrastructure;

import org.example.flyawayapi.flight.dto.NewIdDTO;
import org.example.flyawayapi.user.application.UserService;
import org.example.flyawayapi.user.domain.User;
import org.example.flyawayapi.user.dto.RegisterUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<NewIdDTO> register(@RequestBody RegisterUserDTO newUser) {
        User user = userService.register(newUser);
        return ResponseEntity
                .status(201)
                .body(new NewIdDTO(user.getId().toString()));
    }
}