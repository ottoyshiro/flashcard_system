package com.project.flashcard_system_frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewStartResponse {
    private String sessionId;
    private String userId;
    private List<FlashcardDTO> flashcards;
    private int correctAnswers;
    private int totalAnswers;
}

