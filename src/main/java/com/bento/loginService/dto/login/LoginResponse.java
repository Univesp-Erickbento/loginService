package com.bento.loginService.dto.login;

public record LoginResponse(String accessToken, Long expiresIn) {
}
