package com.bento.loginService.application.services;

import com.bento.loginService.adapters.out.repository.SpringDataLoginRepository;
import com.bento.loginService.domain.model.LoginEntity;
import com.bento.loginService.dto.login.LoginRequest;
import com.bento.loginService.dto.login.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceImplTest {

    private SpringDataLoginRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        userRepository = mock(SpringDataLoginRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        jwtEncoder = mock(JwtEncoder.class);

        loginService = new LoginServiceImpl(jwtEncoder, userRepository, passwordEncoder);
    }

    // === register ===

    @Test
    void testRegisterSuccess() {
        LoginRequest request = new LoginRequest("user", "pass");

        when(userRepository.findByNomeUsuario("user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("hashed_pass");

        String result = loginService.register(request);

        assertEquals("Usuário registrado com sucesso!", result);
        verify(userRepository).save(any(LoginEntity.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        LoginRequest request = new LoginRequest("user", "pass");

        when(userRepository.findByNomeUsuario("user"))
                .thenReturn(Optional.of(new LoginEntity()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginService.register(request);
        });

        assertEquals("Usuário já existe!", exception.getMessage());
    }

    @Test
    void testRegisterWithInvalidInput() {
        LoginRequest request = new LoginRequest("", "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.register(request);
        });

        assertEquals("Nome de usuário e senha são obrigatórios!", exception.getMessage());
    }

    // === login ===

//    @Test
//    void testLoginSuccess() {
//        LoginRequest request = new LoginRequest("user", "pass");
//
//        LoginEntity user = mock(LoginEntity.class);
//        when(userRepository.findByNomeUsuario("user")).thenReturn(Optional.of(user));
//        when(user.isLoginCorrect(request, passwordEncoder)).thenReturn(true);
//
//        Jwt jwt = mock(Jwt.class);
//        when(jwt.getTokenValue()).thenReturn("jwt_token");
//
//        // CORREÇÃO: usar `any(...)` diretamente no when()
//        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
//
//        LoginResponse response = loginService.login(request);
//
//        assertEquals("jwt_token", response.accessToken());
//        assertEquals(300L, response.expiresIn());
//    }

    @Test
    void testLoginInvalidCredentials() {
        LoginRequest request = new LoginRequest("user", "wrong");

        LoginEntity user = mock(LoginEntity.class);
        when(userRepository.findByNomeUsuario("user")).thenReturn(Optional.of(user));
        when(user.isLoginCorrect(request, passwordEncoder)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            loginService.login(request);
        });
    }

    @Test
    void testLoginMissingFields() {
        LoginRequest request = new LoginRequest("", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.login(request);
        });

        assertEquals("Nome de usuário e senha são obrigatórios!", exception.getMessage());
    }

    // === updatePassword ===

    @Test
    void testUpdatePasswordSuccess() {
        LoginRequest request = new LoginRequest("user", "newpass");

        LoginEntity user = new LoginEntity();
        when(userRepository.findByNomeUsuario("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("hashed_newpass");

        String result = loginService.updatePassword("user", request);

        assertEquals("Senha atualizada com sucesso!", result);
        verify(userRepository).save(user);
        assertEquals("hashed_newpass", user.getSenha());
    }

    @Test
    void testUpdatePasswordUserNotFound() {
        LoginRequest request = new LoginRequest("user", "newpass");

        when(userRepository.findByNomeUsuario("user")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginService.updatePassword("user", request);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
    }

    @Test
    void testUpdatePasswordEmpty() {
        LoginRequest request = new LoginRequest("user", "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.updatePassword("user", request);
        });

        assertEquals("Senha é obrigatória!", exception.getMessage());
    }

    // === deleteUser ===

    @Test
    void testDeleteUserSuccess() {
        LoginEntity user = new LoginEntity();
        when(userRepository.findByNomeUsuario("user")).thenReturn(Optional.of(user));

        String result = loginService.deleteUser("user");

        assertEquals("Usuário excluído com sucesso!", result);
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findByNomeUsuario("user")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginService.deleteUser("user");
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
    }
}
