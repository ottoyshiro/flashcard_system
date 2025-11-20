package com.project.flashcard_system.useCases.user;

import com.project.flashcard_system.models.User;
import com.project.flashcard_system.repositories.UsersRepository;
import com.project.flashcard_system.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class UserLoginValidationUseCase {

    private final UsersRepository repository;
    private final JwtService jwtService;

    public UserLoginValidationUseCase(UsersRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    public String execute(User user) {
        User userFromDb = repository.findByUsername(user.getUsername());
        if(userFromDb != null && userFromDb.getPassword().equals(user.getPassword())) {
            return jwtService.generateToken(userFromDb.getId());
        }
        return null;
    }
}
