package com.project.flashcard_system_frontend;

public class AuthStore {

    private static String token;

    public static void setToken(String jwtToken) {
        token = jwtToken;
    }

    public static String getToken() {
        return token;
    }

    public static boolean isLoggedIn() {
        return token != null && !token.isEmpty();
    }

    public static void clear() {
        token = null;
    }
}


