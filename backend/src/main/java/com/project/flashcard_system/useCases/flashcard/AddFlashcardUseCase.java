package com.project.flashcard_system.useCases.flashcard;

import com.project.flashcard_system.models.Flashcard;
import com.project.flashcard_system.repositories.FlashcardsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddFlashcardUseCase {

    private final FlashcardsRepository repository;

    public AddFlashcardUseCase(FlashcardsRepository repository) {
        this.repository = repository;
    }

    public void execute(Flashcard flashcard) {
        flashcard.setId(UUID.randomUUID());
        repository.save(flashcard);
    }
}
