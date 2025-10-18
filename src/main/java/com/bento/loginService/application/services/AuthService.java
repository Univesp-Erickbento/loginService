package com.bento.loginService.application.services;



import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String getToken(String authorizationHeader) {
        // Verifica se o cabeçalho de autorização está presente e começa com "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token de autorização inválido ou não fornecido.");
        }

        // Extrai o token do cabeçalho (remove o prefixo "Bearer ")
        String token = authorizationHeader.substring(7); // "Bearer <token>" -> "<token>"

        // Aqui você pode adicionar mais lógica para validar o token (ex: decodificar JWT, verificar validade, etc.)
        // Por enquanto, estamos apenas validando a estrutura do token

        // Log para confirmar se o token está correto
        System.out.println("Token recebido: " + token);

        // Caso precise de mais validações ou armazenamentos do token, adicione aqui

        return token;
    }
}
