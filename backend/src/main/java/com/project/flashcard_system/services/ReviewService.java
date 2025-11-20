package com.project.flashcard_system.services;

import com.project.flashcard_system.models.Flashcard;
import com.project.flashcard_system.models.ReviewSession;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReviewService {

    // Mapa de sessões ativas: sessionId -> ReviewSession
    private final Map<UUID, ReviewSession> activeSessions = new ConcurrentHashMap<>();

    /**
     * Inicia uma nova sessão de revisão para o usuário.
     * Se já existir uma sessão anterior, ela é sobrescrita.
     */
    public ReviewSession startSession(UUID userId, List<Flashcard> flashcards) {
        UUID sessionId = UUID.randomUUID();

        ReviewSession session = new ReviewSession(sessionId, userId, flashcards, 0, 0);

        activeSessions.put(sessionId, session);
        return session;
    }

    /**
     * Registra uma resposta do usuário para um flashcard específico.
     */
    public ReviewSession answer(UUID sessionId, UUID flashcardId, int selectedAnswer) {
        ReviewSession session = activeSessions.get(sessionId);
        if (session == null) throw new RuntimeException("Sessão não encontrada.");

        session.setTotalAnswers(session.getTotalAnswers() + 1);

        Flashcard flashcard = session.getFlashcards().stream()
                .filter(f -> f.getId().equals(flashcardId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Flashcard não encontrado."));

        if (flashcard.getCorrectAnswer() == selectedAnswer) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        }

        return session;
    }

    /**
     * Finaliza a sessão e a remove da memória.
     */
    public ReviewSession finish(UUID sessionId) {
        ReviewSession session = activeSessions.remove(sessionId);
        if (session == null) throw new RuntimeException("Sessão não encontrada.");
        return session;
    }

    /**
     * Recupera uma sessão ativa por ID.
     */
    public ReviewSession getSession(UUID sessionId) {
        return activeSessions.get(sessionId);
    }

    /**
     * Recupera todas as sessões de um usuário (caso tenha múltiplas).
     */
    public List<ReviewSession> getSessionsByUser(UUID userId) {
        return activeSessions.values().stream()
                .filter(s -> s.getUserId().equals(userId))
                .toList();
    }
}
