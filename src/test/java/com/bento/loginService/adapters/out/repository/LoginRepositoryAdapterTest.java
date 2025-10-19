package com.bento.loginService.adapters.out.repository;

import com.bento.loginService.domain.model.LoginEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginRepositoryAdapterTest {

    private SpringDataLoginRepository springDataLoginRepository;
    private LoginRepositoryAdapter loginRepositoryAdapter;

    @BeforeEach
    void setUp() {
        springDataLoginRepository = mock(SpringDataLoginRepository.class);
        loginRepositoryAdapter = new LoginRepositoryAdapter(springDataLoginRepository);
    }

    @Test
    void testFindByNomeUsuarioReturnsUser() {
        // Arrange
        String nomeUsuario = "usuario1";
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setId(1L);
        loginEntity.setNomeUsuario("usuario1");
        loginEntity.setSenha("senha123");

        when(springDataLoginRepository.findByNomeUsuario(nomeUsuario))
                .thenReturn(Optional.of(loginEntity));

        // Act
        Optional<LoginEntity> result = loginRepositoryAdapter.findByNomeUsuario(nomeUsuario);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("usuario1", result.get().getNomeUsuario());
        assertEquals("senha123", result.get().getSenha());
    }

    @Test
    void testFindByNomeUsuarioReturnsEmpty() {
        String nomeUsuario = "inexistente";

        when(springDataLoginRepository.findByNomeUsuario(nomeUsuario))
                .thenReturn(Optional.empty());

        Optional<LoginEntity> result = loginRepositoryAdapter.findByNomeUsuario(nomeUsuario);

        assertFalse(result.isPresent());
    }

    @Test
    void testSaveReturnsSavedEntity() {
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setId(1L);
        loginEntity.setNomeUsuario("usuario2");
        loginEntity.setSenha("senha123");

        when(springDataLoginRepository.save(loginEntity)).thenReturn(loginEntity);

        LoginEntity saved = loginRepositoryAdapter.save(loginEntity);

        assertNotNull(saved);
        assertEquals("usuario2", saved.getNomeUsuario());
    }
}
