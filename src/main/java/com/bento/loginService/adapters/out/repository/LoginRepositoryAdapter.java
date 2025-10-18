package com.bento.loginService.adapters.out.repository;

import com.bento.loginService.domain.model.LoginEntity;
import com.bento.loginService.ports.out.LoginRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginRepositoryAdapter implements LoginRepositoryPort {

    private final SpringDataLoginRepository springDataLoginRepository;

    public LoginRepositoryAdapter(SpringDataLoginRepository springDataLoginRepository) {
        this.springDataLoginRepository = springDataLoginRepository;
    }

    @Override
    public Optional<LoginEntity> findByNomeUsuario(String nomeUsuario) {
        return springDataLoginRepository.findByNomeUsuario(nomeUsuario);
    }

    @Override
    public LoginEntity save(LoginEntity loginEntity) {
        return springDataLoginRepository.save(loginEntity);
    }
}