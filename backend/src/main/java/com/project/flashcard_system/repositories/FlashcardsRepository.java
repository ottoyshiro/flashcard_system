package com.project.flashcard_system.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.flashcard_system.models.Flashcard;
import org.springframework.stereotype.Repository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FlashcardsRepository {

    private final Path dirPath = Paths.get("data");
    private final Path filePath = dirPath.resolve("flashcards.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public FlashcardsRepository() {
        try {
            if(!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            if(!Files.exists(filePath)) {
                Files.createFile(filePath);

                mapper.writeValue(filePath.toFile(), new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar o repositório", e);
        }
    }

    public List<Flashcard> list() {
        try {
            return Arrays.asList(mapper.readValue(filePath.toFile(), Flashcard[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Flashcard flashcard) {
        List<Flashcard> flashcards = new ArrayList<>(list());
        flashcards.add(flashcard);
        write(flashcards);
    }

    public void write(List<Flashcard> flashcards) {
        try {
            mapper.writeValue(filePath.toFile(), flashcards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Flashcard updatedFlashcard, Flashcard flashcard) {
        flashcard.setContent(updatedFlashcard.getContent());
        flashcard.setSubject(updatedFlashcard.getSubject());
        flashcard.setAnswers(updatedFlashcard.getAnswers());
        flashcard.setCorrectAnswer(updatedFlashcard.getCorrectAnswer());
    }

    public void delete(Flashcard flashcard) {
        List<Flashcard> flashcards = new ArrayList<>(list());
        flashcards.remove(flashcard);
        write(flashcards);
    }

    public List<Flashcard> findByUserId(UUID userId) {
        return list().stream()
                .filter(f -> f.getUserId() != null && f.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Flashcard> findByUserIdAndIds(UUID userId, List<UUID> flashcardIds) {
        return list().stream()
                .filter(f -> f.getUserId() != null
                        && f.getUserId().equals(userId)
                        && flashcardIds.contains(f.getId()))
                .collect(Collectors.toList());
    }
}
