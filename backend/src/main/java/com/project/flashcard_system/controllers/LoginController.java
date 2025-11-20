package com.project.flashcard_system.controllers;

import com.project.flashcard_system.models.User;
import com.project.flashcard_system.useCases.user.UserLoginValidationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserLoginValidationUseCase userLoginValidation;

    @PostMapping
    public ResponseEntity<Map<String, String>> userLogin(@RequestBody User user) {
        String token = userLoginValidation.execute(user);
        if (token != null) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(Map.of("token", token));
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuário e/ou senha inválido(s)!"));
    }

}
