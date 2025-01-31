package com.github.jessicaraposo.service;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "jwt.secret=SuperSecretKeyWithAtLeast32CharactersLong")
class JwtServiceTestIT {

    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String secret;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secret);
    }

    @Test
    void shouldGenerateAndValidateTokenSuccessfully() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "Admin");
        claims.put("Seed", 7); // Número primo válido

        String token = jwtService.generateToken(claims);
        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void shouldFailValidationForInvalidToken() {
        String invalidToken = "invalid.token.here";

        assertThrows(MalformedJwtException.class, () -> jwtService.validateToken(invalidToken));
    }

    @Test
    void shouldFailValidationForTokenWithInvalidSeed() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "Admin");
        claims.put("Seed", 8); // Não é primo

        String token = jwtService.generateToken(claims);
        assertNotNull(token, "O token não foi gerado corretamente.");
        System.out.println("Token gerado: " + token);
        Exception exception = assertThrows(Exception.class, () -> jwtService.validateToken(token));
        assertEquals("Claim 'Seed' deve ser um número primo.", exception.getMessage());
    }

    @Test
    void shouldFailValidationForTokenWithInvalidRole() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "InvalidRole");
        claims.put("Seed", 7);

        String token = jwtService.generateToken(claims);
        Exception exception = assertThrows(MalformedJwtException.class, () -> jwtService.validateToken(token));
        assertTrue(exception.getMessage().contains("Claim 'Role' inválida"));
    }

    @Test
    void shouldFailValidationForTokenWithInvalidName() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User123"); // Contém números
        claims.put("Role", "Admin");
        claims.put("Seed", 7);

        String token = jwtService.generateToken(claims);
        Exception exception = assertThrows(MalformedJwtException.class, () -> jwtService.validateToken(token));
        assertTrue(exception.getMessage().contains("Claim 'Name' contém caracteres inválidos"));
    }

    @Test
    void shouldFailValidationForTokenWithInvalidSignature() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "Admin");
        claims.put("Seed", 7);

        String token = jwtService.generateToken(claims);
        String tamperedToken = token + "tampered";

        assertThrows(SignatureException.class, () -> jwtService.validateToken(tamperedToken));
    }
}
