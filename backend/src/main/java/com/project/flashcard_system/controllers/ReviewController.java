package com.project.flashcard_system.controllers;

import com.project.flashcard_system.models.ReviewSession;
import com.project.flashcard_system.security.JwtService;
import com.project.flashcard_system.services.ReviewService;
import com.project.flashcard_system.useCases.flashcard.review.StartFlashcardReviewUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private StartFlashcardReviewUseCase startFlashcardReview;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/start")
    public ResponseEntity<ReviewSession> startReview(
            @RequestBody(required = false)List<UUID> flashcardIds,
            @RequestHeader("Authorization") String token) {

        UUID userId = jwtService.extractUserId(token.replace("Bearer ", ""));
        ReviewSession session = startFlashcardReview.execute(userId, flashcardIds);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{sessionId}/answer")
    public ResponseEntity<ReviewSession> aswerFlashcard(
            @PathVariable UUID sessionId,
            @RequestParam UUID flashcardId,
            @RequestParam int selectedAnswer) {

        ReviewSession updatedSession = reviewService.answer(sessionId, flashcardId, selectedAnswer);
        return ResponseEntity.ok(updatedSession);
    }

    @PostMapping("/{sessionId}/finish")
    public ResponseEntity<ReviewSession> finishReview(@PathVariable UUID sessionId) {
        ReviewSession finished = reviewService.finish(sessionId);
        return ResponseEntity.ok(finished);
    }
}
