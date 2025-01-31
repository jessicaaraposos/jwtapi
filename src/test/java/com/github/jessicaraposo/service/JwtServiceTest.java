package com.github.jessicaraposo.service;

import com.github.jessicaraposo.util.PrimeNumberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private static final String SECRET_KEY = "01234567890123456789012345678901"; // 32 caracteres

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET_KEY);
    }

    @Test
    void testGenerateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "John Doe");
        claims.put("Role", "Admin");
        claims.put("Seed", 7);

        String token = jwtService.generateToken(claims);
        assertNotNull(token, "O token gerado não deve ser nulo");
    }

    @Test
    void testValidateToken_ValidToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Alice");
        claims.put("Role", "Member");
        claims.put("Seed", 11); // Número primo

        String token = jwtService.generateToken(claims);

        try (MockedStatic<PrimeNumberUtil> primeMock = mockStatic(PrimeNumberUtil.class)) {
            primeMock.when(() -> PrimeNumberUtil.isPrime(11)).thenReturn(true);
            assertTrue(jwtService.validateToken(token), "O token válido deve ser aceito");
        }
    }

    @Test
    void testValidateToken_InvalidRole() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Alice");
        claims.put("Role", "InvalidRole");
        claims.put("Seed", 11);

        String token = jwtService.generateToken(claims);

        assertFalse(jwtService.validateToken(token), "O token com Role inválida deveria ser rejeitado");
    }

    @Test
    void testValidateToken_InvalidSeed_NotPrime() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Alice");
        claims.put("Role", "Member");
        claims.put("Seed", 10); // Número que não é primo

        String token = jwtService.generateToken(claims);

        assertFalse(jwtService.validateToken(token), "O token com Seed não primo deveria ser rejeitado");
    }

    @Test
    void testValidateToken_InvalidName_ContainsNumber() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Alice123"); // Nome inválido
        claims.put("Role", "Member");
        claims.put("Seed", 11);

        String token = jwtService.generateToken(claims);

        assertFalse(jwtService.validateToken(token), "O token com Name inválido deveria ser rejeitado");
    }

    @Test
    void testValidateToken_InvalidSeed_NotInteger() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "TextoEm");
        claims.put("Role", "Member");
        claims.put("Seed", "NotANumber"); // Seed inválido

        String token = jwtService.generateToken(claims);

        assertFalse(jwtService.validateToken(token), "O token com Seed inválido deveria ser rejeitado");
    }
}
