package com.bento.loginService.application.services;

import com.bento.loginService.dto.login.LoginRequest;
import com.bento.loginService.domain.model.LoginEntity;
import com.bento.loginService.adapters.out.repository.SpringDataLoginRepository;
import com.bento.loginService.dto.login.LoginResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class LoginServiceImpl {

    private final JwtEncoder jwtEncoder;
    private final SpringDataLoginRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginServiceImpl(JwtEncoder jwtEncoder,
                            SpringDataLoginRepository userRepository,
                            BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(LoginRequest loginRequest) {
        // Verificar se os dados de entrada são válidos (exemplo: senha e nome de usuário não vazios)
        if (loginRequest.nomeUsuario() == null || loginRequest.nomeUsuario().isEmpty() || loginRequest.senha() == null || loginRequest.senha().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário e senha são obrigatórios!");
        }

        // Verificar se o usuário já existe
        Optional<LoginEntity> user = userRepository.findByNomeUsuario(loginRequest.nomeUsuario());

        if (user.isPresent()) {
            throw new RuntimeException("Usuário já existe!");
        }

        // Criar novo usuário
        LoginEntity newUser = new LoginEntity();
        newUser.setNomeUsuario(loginRequest.nomeUsuario());
        newUser.setSenha(passwordEncoder.encode(loginRequest.senha()));  // Senha criptografada

        // Salvar no banco
        userRepository.save(newUser);

        return "Usuário registrado com sucesso!";
    }

    public LoginResponse login(LoginRequest loginRequest) {
        if (loginRequest.nomeUsuario() == null || loginRequest.nomeUsuario().isEmpty() || loginRequest.senha() == null || loginRequest.senha().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário e senha são obrigatórios!");
        }

        Optional<LoginEntity> user = userRepository.findByNomeUsuario(loginRequest.nomeUsuario());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha inválidos!");
        }

        return createJwtResponse(user.get());
    }

    private LoginResponse createJwtResponse(LoginEntity user) {
        var now = Instant.now();
        var expiresIn = 300L;  // Tempo de expiração do JWT

        var claims = JwtClaimsSet.builder()
                .issuer("serviço-de-cadastro-BMT")
                .subject(user.getNomeUsuario())  // Nome de usuário no JWT
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, expiresIn);
    }

    public String updatePassword(String username, LoginRequest loginRequest) {
        if (loginRequest.senha() == null || loginRequest.senha().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória!");
        }

        Optional<LoginEntity> user = userRepository.findByNomeUsuario(username);

        if (user.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado!");
        }

        LoginEntity existingUser = user.get();
        existingUser.setSenha(passwordEncoder.encode(loginRequest.senha()));

        userRepository.save(existingUser);

        return "Senha atualizada com sucesso!";
    }

    public String deleteUser(String username) {
        Optional<LoginEntity> user = userRepository.findByNomeUsuario(username);

        if (user.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado!");
        }

        userRepository.delete(user.get());

        return "Usuário excluído com sucesso!";
    }
}
