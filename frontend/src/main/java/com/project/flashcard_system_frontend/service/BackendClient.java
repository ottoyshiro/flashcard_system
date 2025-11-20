package com.project.flashcard_system_frontend.service;

import com.project.flashcard_system_frontend.AuthStore;
import okhttp3.*;

public class BackendClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080";

    public static boolean registerUser(String username, String password) throws Exception {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/user")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.code() == 201;
        }
    }

    public static boolean loginUser(String username, String password) throws Exception {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() != 202) {
                return false;
            }

            String responseBody = response.body().string();
            System.out.println("LOGIN RESPONSE BODY: " + responseBody);

            // pegar o token do JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();

            String token = mapper.readTree(responseBody).get("token").asText();

            System.out.println("TOKEN PARSED: " + token);

            // salvar no AuthStore
            AuthStore.setToken(token);

            return true;
        }
    }

}

