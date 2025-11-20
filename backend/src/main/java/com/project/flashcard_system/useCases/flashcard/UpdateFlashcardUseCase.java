package com.project.flashcard_system.useCases.flashcard;

import com.project.flashcard_system.models.Flashcard;
import com.project.flashcard_system.repositories.FlashcardsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UpdateFlashcardUseCase {

    private final FlashcardsRepository repository;

    public UpdateFlashcardUseCase(FlashcardsRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Flashcard updatedFlashcard, UUID userId) {
        List<Flashcard> flashcards = new ArrayList<>(repository.list());

        if(flashcards.stream().anyMatch(f -> f.getUserId().equals(userId))) {
            for (Flashcard flashcard : flashcards) {
                if (flashcard.getId().equals(updatedFlashcard.getId())) {
                    repository.update(updatedFlashcard, flashcard);
                    repository.write(flashcards);
                    return true;
                }
            }
        }
        return false;
    }
}
