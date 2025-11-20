package com.project.flashcard_system.useCases.flashcard;

import com.project.flashcard_system.models.Flashcard;
import com.project.flashcard_system.repositories.FlashcardsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ListFlashcardsUseCase {

    private final FlashcardsRepository repository;

    public ListFlashcardsUseCase(FlashcardsRepository repository) {
        this.repository = repository;
    }

    public List<Flashcard> execute(UUID userId) {
        List<Flashcard> flashcards = new ArrayList<>(repository.list());

        return flashcards.stream().filter(f -> f.getUserId().equals(userId)).toList();
    }
}
