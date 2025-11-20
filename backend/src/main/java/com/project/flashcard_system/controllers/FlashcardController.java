package com.project.flashcard_system.controllers;

import com.project.flashcard_system.models.Flashcard;
import com.project.flashcard_system.security.JwtService;
import com.project.flashcard_system.useCases.flashcard.AddFlashcardUseCase;
import com.project.flashcard_system.useCases.flashcard.DeleteFlashcardUseCase;
import com.project.flashcard_system.useCases.flashcard.ListFlashcardsUseCase;
import com.project.flashcard_system.useCases.flashcard.UpdateFlashcardUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flashcard")
public class FlashcardController {

    @Autowired
    private AddFlashcardUseCase addFlashcard;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<String> addFlashcard(@RequestBody Flashcard flashcard, @RequestHeader("Authorization") String token) {

        UUID userId = jwtService.extractUserId(token.replace("Bearer ", ""));
        flashcard.setUserId(userId);

        addFlashcard.execute(flashcard);
        return ResponseEntity.status(HttpStatus.CREATED).body("Flashcard criado com sucesso!");
    }

    @Autowired
    private UpdateFlashcardUseCase updateFlashcard;

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFlashcard(@RequestBody Flashcard flashcard, @PathVariable UUID id, @RequestHeader("Authorization") String token) {
        UUID userId = jwtService.extractUserId(token.replace("Bearer ", ""));

        flashcard.setId(id); //como ele nao passa no add, logo ele nao tem id, somente o corpo
        if(updateFlashcard.execute(flashcard, userId)) {
            return ResponseEntity.status(HttpStatus.GONE).body("Flashcard atualizado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flashcard não existe ou não pertence ao usuário. ");
        }
    }

    @Autowired
    private ListFlashcardsUseCase listFlashcards;

    @GetMapping
    public ResponseEntity<List<Flashcard>> ListFlashcards (@RequestHeader("Authorization") String token){
        UUID userId = jwtService.extractUserId(token.replace("Bearer ", ""));

        List<Flashcard> flashcards = listFlashcards.execute(userId);
        return ResponseEntity.ok(flashcards);
    }

    @Autowired
    private DeleteFlashcardUseCase deleteFlashcard;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlashcard(@PathVariable UUID id, @RequestHeader("Authorization") String token) {
        UUID userId = jwtService.extractUserId(token.replace("Bearer ", ""));

        if(deleteFlashcard.execute(id, userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Flashcard deletado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flashcard não existe ou não pertence ao usuário.");
        }
    }
}
