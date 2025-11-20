package com.project.flashcard_system.useCases.flashcard.review;

import com.project.flashcard_system.models.Flashcard;
import com.project.flashcard_system.models.ReviewSession;
import com.project.flashcard_system.repositories.FlashcardsRepository;
import com.project.flashcard_system.security.JwtService;
import com.project.flashcard_system.services.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StartFlashcardReviewUseCase {

    private final FlashcardsRepository repository;
    private final ReviewService reviewService;

    public StartFlashcardReviewUseCase(FlashcardsRepository repository, ReviewService reviewService) {
        this.repository = repository;
        this.reviewService = reviewService;
    }

    public ReviewSession execute(UUID userId, List<UUID> flashcardIds) {
        List<Flashcard> flashcards;

        if (flashcardIds == null || flashcardIds.isEmpty()) {
            // Modo automático
            flashcards = repository.findByUserId(userId);
        } else {
            // Modo personalizado
            flashcards = repository.findByUserIdAndIds(userId, flashcardIds);
        }

        return reviewService.startSession(userId, flashcards);
    }

}
