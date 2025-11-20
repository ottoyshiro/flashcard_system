package com.project.flashcard_system.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flashcard {
    private UUID id;
    private UUID userId;
    private String content;
    private String subject;
    private List<String> answers;
    private int correctAnswer;
}
