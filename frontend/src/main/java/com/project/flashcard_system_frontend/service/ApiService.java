package com.project.flashcard_system_frontend.service;

import com.project.flashcard_system_frontend.AuthStore;
import com.project.flashcard_system_frontend.model.FlashcardDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8080";

    public static List<FlashcardDTO> getAllFlashcards() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/flashcard"))
                    .header("Authorization", "Bearer " + AuthStore.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS: " + response.statusCode());
            System.out.println("BODY: " + response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro ao buscar flashcards: " + response.body());
            }

            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(response.body(), FlashcardDTO[].class));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

