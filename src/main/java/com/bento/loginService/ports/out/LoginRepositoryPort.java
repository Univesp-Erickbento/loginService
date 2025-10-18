package com.bento.loginService.ports.out;

import com.bento.loginService.domain.model.LoginEntity;

import java.util.Optional;

public interface LoginRepositoryPort {

    Optional<LoginEntity> findByNomeUsuario(String nomeUsuario);

    LoginEntity save(LoginEntity loginEntity);
}
