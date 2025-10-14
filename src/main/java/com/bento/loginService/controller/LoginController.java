package com.bento.loginService.controller;

import com.bento.loginService.dto.login.LoginRequest;
import com.bento.loginService.services.LoginService;
import com.bento.loginService.dto.login.LoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://45.93.100.30:4200", "http://192.168.15.2:4200", "http://192.168.15.200:4200", "http://localhost:4200"})
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // Endpoint para criar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest) {
        try {
            String response = loginService.register(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Mensagem de erro para caso de usuário já existente
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    // Endpoint para autenticação do usuário (login)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = loginService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);  // Unauthorized, credenciais erradas
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);  // Erro genérico
        }
    }

    // Endpoint para atualizar a senha de um usuário
    @PutMapping("/update/{username}")
    public ResponseEntity<String> updatePassword(@PathVariable String username, @RequestBody LoginRequest loginRequest) {
        try {
            String response = loginService.updatePassword(username, loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Usuário não encontrado
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    // Endpoint para excluir um usuário
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            String response = loginService.deleteUser(username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Usuário não encontrado
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }
}
