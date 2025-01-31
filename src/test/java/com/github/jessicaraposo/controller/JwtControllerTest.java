package com.github.jessicaraposo.controller;

import com.github.jessicaraposo.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtControllerTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtController jwtController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldValidateJwtSuccessfully() {
        String validToken = "Bearer valid.jwt.token";
        when(jwtService.validateToken("valid.jwt.token")).thenReturn(true);

        ResponseEntity<Map<String, Object>> response = jwtController.validateJwt(validToken);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Token válido", response.getBody().get("message"));
    }

    @Test
    void shouldRejectInvalidJwt() {
        String invalidToken = "Bearer invalid.jwt.token";
        when(jwtService.validateToken("invalid.jwt.token")).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = jwtController.validateJwt(invalidToken);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Token inválido", response.getBody().get("message"));
    }

    @Test
    void shouldReturnBadRequestForInvalidFormat() {
        String malformedToken = "invalidFormatToken";

        ResponseEntity<Map<String, Object>> response = jwtController.validateJwt(malformedToken);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Formato inválido do token. Use 'Bearer <token>'", response.getBody().get("message"));
    }

    @Test
    void shouldGenerateJwtSuccessfully() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "Admin");
        claims.put("Seed", 7);

        when(jwtService.generateToken(claims)).thenReturn("generated.jwt.token");

        ResponseEntity<Map<String, Object>> response = jwtController.generateJwt(claims);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().get("status"));
        assertNotNull(response.getBody().get("token"));
        assertEquals("generated.jwt.token", response.getBody().get("token"));
    }

    @Test
    void shouldReturnBadRequestWhenJwtGenerationFails() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "Admin");
        claims.put("Seed", 7);

        when(jwtService.generateToken(claims)).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<Map<String, Object>> response = jwtController.generateJwt(claims);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Erro ao gerar o token", response.getBody().get("message"));
    }
}
