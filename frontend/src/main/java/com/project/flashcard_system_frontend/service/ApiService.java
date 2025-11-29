package com.project.flashcard_system_frontend.service;

import com.project.flashcard_system_frontend.AuthStore;
import com.project.flashcard_system_frontend.model.FlashcardDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.flashcard_system_frontend.model.ReviewStartResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8080";

    // -- FLASHCARD MANAGEMENT --
    public static List<FlashcardDTO> getAllFlashcards() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/flashcard"))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro ao buscar flashcards: " + response.body());
            }

            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(response.body(), FlashcardDTO[].class));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean newFlashcard(FlashcardDTO dto) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // converte o DTO em JSON
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/flashcard"))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 201 || response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFlashcard(FlashcardDTO dto) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String flashcardId = dto.getId();

            String url = String.format("%s/flashcard/%s", BASE_URL, flashcardId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 || response.statusCode() == 204;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean editFlashcard(FlashcardDTO dto) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String flashcardId = dto.getId();
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(dto);

            String url = String.format("%s/flashcard/%s", BASE_URL, flashcardId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 410 || response.statusCode() == 404;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // -- REVIEW MANAGEMENT --
    public static ReviewStartResponse startReview() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("%s/review/start", BASE_URL);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            if (response.statusCode() != 200) {
                System.out.println("Erro ao iniciar revisão: " + response.body());
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            ReviewStartResponse data = mapper.readValue(response.body(), ReviewStartResponse.class);
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean answerFlashcard(String sessionId, String flashcardId, int selectedAnswer) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("%s/review/%s/answer?flashcardId=%s&selectedAnswer=%d", BASE_URL, sessionId, flashcardId, selectedAnswer);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            return response.statusCode() == 200 || response.statusCode() == 500;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ReviewStartResponse finishReview(String sessionId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = String.format("%s/review/%s/finish", BASE_URL, sessionId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            if (response.statusCode() != 200) {
                System.out.println("Erro ao finalizar revisão: " + response.body());
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            ReviewStartResponse data = mapper.readValue(response.body(), ReviewStartResponse.class);
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}

