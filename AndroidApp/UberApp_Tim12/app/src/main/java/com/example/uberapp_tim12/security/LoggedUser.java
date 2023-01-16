package com.example.uberapp_tim12.security;

public class LoggedUser {
    private static Integer userId;
    private static String role;
    private static String username;
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        LoggedUser.token = token;
    }

    public static Integer getUserId() {
        return userId;
    }

    public static void setUserId(Integer userId) {
        LoggedUser.userId = userId;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        LoggedUser.role = role;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LoggedUser.username = username;
    }
}
