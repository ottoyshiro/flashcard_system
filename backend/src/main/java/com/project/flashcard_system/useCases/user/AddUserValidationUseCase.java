package com.project.flashcard_system.useCases.user;

import com.project.flashcard_system.models.User;
import com.project.flashcard_system.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddUserValidationUseCase {
    private final UsersRepository repository;

    public AddUserValidationUseCase(UsersRepository repository) {
        this.repository = repository;
    }

    public boolean userValidation(User checkedUser) {
        return repository.list().stream()
                .noneMatch(u -> u.getUsername().equals(checkedUser.getUsername()));
    }
    public boolean execute(User user) {
        if(userValidation(user)) {
            user.setId(UUID.randomUUID());
            repository.save(user);
            return true;
        }
        return false;

    }
}
