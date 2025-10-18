package com.bento.loginService.adapters.out.repository;

import com.bento.loginService.domain.model.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataLoginRepository extends JpaRepository<LoginEntity, Long> {

    // Método para buscar uma pessoa pelo nome de usuario
    Optional<LoginEntity> findByNomeUsuario(String nomeUsuario);
}
