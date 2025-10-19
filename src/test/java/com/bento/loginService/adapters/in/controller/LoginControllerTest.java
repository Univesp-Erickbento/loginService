package com.bento.loginService.adapters.in.controller;

import com.bento.loginService.application.services.LoginServiceImpl;
import com.bento.loginService.dto.login.LoginRequest;
import com.bento.loginService.dto.login.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private LoginServiceImpl loginService;
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginServiceImpl.class);
        loginController = new LoginController(loginService);
    }

    @Test
    void testRegisterSuccess() {
        LoginRequest request = new LoginRequest("user", "pass");
        when(loginService.register(request)).thenReturn("Usuário criado com sucesso");

        ResponseEntity<String> response = loginController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuário criado com sucesso", response.getBody());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        LoginRequest request = new LoginRequest("user", "pass");
        when(loginService.register(request)).thenThrow(new RuntimeException("Usuário já existe"));

        ResponseEntity<String> response = loginController.register(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Usuário já existe", response.getBody());
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest("user", "pass");
        LoginResponse expected = new LoginResponse("token", 3000L);

        when(loginService.login(request)).thenReturn(expected);

        ResponseEntity<LoginResponse> response = loginController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testLoginBadCredentials() {
        LoginRequest request = new LoginRequest("user", "wrongpass");

        when(loginService.login(request)).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        ResponseEntity<LoginResponse> response = loginController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testUpdatePasswordSuccess() {
        LoginRequest request = new LoginRequest("user", "newpass");

        when(loginService.updatePassword("user", request)).thenReturn("Senha atualizada");

        ResponseEntity<String> response = loginController.updatePassword("user", request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Senha atualizada", response.getBody());
    }

    @Test
    void testUpdatePasswordUserNotFound() {
        LoginRequest request = new LoginRequest("user", "newpass");

        when(loginService.updatePassword("user", request)).thenThrow(new RuntimeException("Usuário não encontrado"));

        ResponseEntity<String> response = loginController.updatePassword("user", request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    void testDeleteUserSuccess() {
        when(loginService.deleteUser("user")).thenReturn("Usuário deletado");

        ResponseEntity<String> response = loginController.deleteUser("user");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuário deletado", response.getBody());
    }

    @Test
    void testDeleteUserNotFound() {
        when(loginService.deleteUser("user")).thenThrow(new RuntimeException("Usuário não encontrado"));

        ResponseEntity<String> response = loginController.deleteUser("user");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Usuário não encontrado", response.getBody());
    }
}
