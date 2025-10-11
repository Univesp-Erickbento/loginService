package com.bento.loginService.dto.login;

public record LoginRequest(
        String nomeUsuario,
        String senha) {
}