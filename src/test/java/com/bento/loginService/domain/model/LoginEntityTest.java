package com.bento.loginService.domain.model;

import com.bento.loginService.dto.login.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginEntityTest {

    private LoginEntity loginEntity;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        loginEntity = new LoginEntity();
        passwordEncoder = mock(PasswordEncoder.class);
    }

    @Test
    void testSetAndGetNomeUsuario() {
        String nomeUsuario = "usuarioTeste";
        loginEntity.setNomeUsuario(nomeUsuario);
        assertEquals(nomeUsuario, loginEntity.getNomeUsuario());
    }

    @Test
    void testSetAndGetSenha() {
        String senha = "senha123";
        loginEntity.setSenha(senha);
        assertEquals(senha, loginEntity.getSenha());
    }

    @Test
    void testSetAndGetId() {
        Long id = 10L;
        loginEntity.setId(id);
        assertEquals(id, loginEntity.getId());
    }

    @Test
    void testSetAndGetPessoaId() {
        long pessoaId = 5L;
        loginEntity.setPessoaId(pessoaId);
        assertEquals(pessoaId, loginEntity.getPessoaId());
    }

    @Test
    void testSetAndGetPerfis() {
        String perfis = "ADMIN,USER";
        loginEntity.setPerfis(perfis);
        assertEquals(perfis, loginEntity.getPerfis());
    }

    @Test
    void testIsLoginCorrect_WithMatchingPassword() {
        LoginRequest loginRequest = mock(LoginRequest.class);
        when(loginRequest.senha()).thenReturn("senhaTeste");

        // Vamos supor que a senha armazenada seja a hash da senha "senhaTeste"
        loginEntity.setSenha("$2a$10$7qH/Izp9fJqE5Y2g8Bx2Muvk8rZDeylG3GoT1b0h3P2H7p6uKu3Q6"); // bcrypt hashed senhaTeste

        // Mock do PasswordEncoder para retornar true quando matches for chamado
        when(passwordEncoder.matches("senhaTeste", loginEntity.getSenha())).thenReturn(true);

        assertTrue(loginEntity.isLoginCorrect(loginRequest, passwordEncoder));
    }

    @Test
    void testIsLoginCorrect_WithNonMatchingPassword() {
        LoginRequest loginRequest = mock(LoginRequest.class);
        when(loginRequest.senha()).thenReturn("senhaErrada");

        loginEntity.setSenha("$2a$10$7qH/Izp9fJqE5Y2g8Bx2Muvk8rZDeylG3GoT1b0h3P2H7p6uKu3Q6"); // bcrypt hashed senhaTeste

        when(passwordEncoder.matches("senhaErrada", loginEntity.getSenha())).thenReturn(false);

        assertFalse(loginEntity.isLoginCorrect(loginRequest, passwordEncoder));
    }
}
