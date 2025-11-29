package com.project.flashcard_system.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSession {
    private UUID sessionId;
    private UUID userId;
    private List<Flashcard> flashcards;
    private int correctAnswers = 0;
    private int totalAnswers = 0;
}
