package com.project.flashcard_system.controllers;

import com.project.flashcard_system.models.User;
import com.project.flashcard_system.useCases.user.AddUserValidationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AddUserValidationUseCase addUserValidation;

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (addUserValidation.execute(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("O usuário já existe!");
    }
}
