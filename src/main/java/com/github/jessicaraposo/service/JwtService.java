package com.github.jessicaraposo.service;

import com.github.jessicaraposo.enums.UserRoleEnum;
import com.github.jessicaraposo.util.PrimeNumberUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final Key secretKey;

    private static final Set<String> VALID_ROLES = Set.of("Admin", "Member", "External");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[^0-9]{1,256}$");
    private static final Set<String> REQUIRED_CLAIMS = Set.of("Name", "Role", "Seed");

    public JwtService(@Value("${jwt.secret}") String secret) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("A chave secreta deve ter pelo menos 32 caracteres.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            logger.info("Claims recebidas: {} , valores {}", claims.keySet(), claims.values());

            if (!claims.keySet().equals(REQUIRED_CLAIMS)) {
                throw new MalformedJwtException("Token contém claims inválidas ou extras");
            }

            String name = claims.get("Name", String.class);
            String role = claims.get("Role", String.class);
            int seed;

            try {
                seed = Integer.parseInt(claims.get("Seed").toString());
            } catch (NumberFormatException | NullPointerException e) {
                throw new MalformedJwtException("Claim 'Seed' deve ser um número inteiro válido");
            }

            validateName(name);
            validateRole(role);
            validatePrime(seed);

            return true;

        } catch (ExpiredJwtException e) {
            logger.warn("Token expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("Token não suportado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Token malformado: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("Assinatura inválida: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Erro na validação do token: {}", e.getMessage());
        }
        return false;
    }

    private void validateName(String name) {
        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            throw new MalformedJwtException("Claim 'Name' contém caracteres inválidos. Apenas letras são permitidas.");
        }
    }

    private void validateRole(String role) {
        if (role == null || !UserRoleEnum.isValidRole(role)) {
            throw new MalformedJwtException("Claim 'Role' inválida. Valores permitidos: " + UserRoleEnum.getValidRoles());
        }
    }

    private void validatePrime(Integer seed) {
        if (!PrimeNumberUtil.isPrime(seed)) {
            throw new MalformedJwtException("Claim 'Seed' deve ser um número primo.");
        }
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }
}
