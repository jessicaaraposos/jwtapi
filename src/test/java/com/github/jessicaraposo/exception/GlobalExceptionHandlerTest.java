package com.github.jessicaraposo.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleExpiredJwtException() {
        ExpiredJwtException exception = new ExpiredJwtException(null, null, "Token expirado");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleExpiredJwtException(exception);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Token expirado. Faça login novamente.", response.getBody().get("message"));
    }

    @Test
    void shouldHandleSignatureException() {
        SignatureException exception = new SignatureException("Assinatura inválida");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleSignatureException(exception);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Assinatura do token inválida.", response.getBody().get("message"));
    }

    @Test
    void shouldHandleMalformedJwtException() {
        MalformedJwtException exception = new MalformedJwtException("Token malformado");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleMalformedJwtException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Formato do token JWT inválido.", response.getBody().get("message"));
    }

    @Test
    void shouldHandleJwtException() {
        JwtException exception = new JwtException("Erro de autenticação JWT");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleJwtException(exception);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Erro de autenticação JWT: Erro de autenticação JWT", response.getBody().get("message"));
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception("Erro interno");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Erro interno no servidor. Tente novamente mais tarde.", response.getBody().get("message"));
    }
}
