package com.project.flashcard_system_frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDTO {
    private String id;
    private String userId;
    private String content;
    private String subject;
    private List<String> answers;
    private int correctAnswer;

}

