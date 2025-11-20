package com.project.flashcard_system.useCases.flashcard;

import com.project.flashcard_system.models.Flashcard;
import com.project.flashcard_system.repositories.FlashcardsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DeleteFlashcardUseCase {

    private final FlashcardsRepository repository;

    public DeleteFlashcardUseCase(FlashcardsRepository repository) {
        this.repository = repository;
    }

    public boolean execute(UUID id, UUID userId) {
        List<Flashcard> flashcards = new ArrayList<>(repository.list());
        for (Flashcard flashcard : flashcards) {
            if (flashcard.getUserId().equals(userId) && flashcard.getId().equals(id)) {
                repository.delete(flashcard);
                return true;
            }
        }
        return false;
    }
}
