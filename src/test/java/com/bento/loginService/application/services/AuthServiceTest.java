package com.bento.loginService.application.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    void testGetTokenValidHeader() {
        String header = "Bearer abcdefg12345";
        String token = authService.getToken(header);

        assertEquals("abcdefg12345", token);
    }

    @Test
    void testGetTokenWithNullHeader() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getToken(null);
        });

        assertEquals("Token de autorização inválido ou não fornecido.", exception.getMessage());
    }

    @Test
    void testGetTokenWithInvalidPrefix() {
        String header = "Token abcdefg12345";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getToken(header);
        });

        assertEquals("Token de autorização inválido ou não fornecido.", exception.getMessage());
    }

    @Test
    void testGetTokenWithEmptyBearer() {
        String header = "Bearer ";

        String token = authService.getToken(header);
        assertEquals("", token);  // ainda é válido, apenas token vazio
    }
}
