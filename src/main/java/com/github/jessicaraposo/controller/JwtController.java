package com.github.jessicaraposo.controller;

import com.github.jessicaraposo.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {

    private static final Logger logger = LoggerFactory.getLogger(JwtController.class);
    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Operation(summary = "Valida um JWT", description = "Recebe um token JWT no header Authorization e verifica se é válido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou ausente"),
            @ApiResponse(responseCode = "400", description = "Formato inválido do token")
    })
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateJwt(
            @RequestHeader("Authorization")
            @Parameter(description = "Token JWT no formato 'Bearer <token>'") String authorizationHeader) {

        logger.info("Recebida solicitação para validar token");

        Map<String, Object> response = new HashMap<>();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.put("status", "error");
            response.put("message", "Formato inválido do token. Use 'Bearer <token>'");
            return ResponseEntity.badRequest().body(response);
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        boolean isValid = jwtService.validateToken(token);

        if (isValid) {
            response.put("status", "success");
            response.put("message", "Token válido");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Token inválido");
            return ResponseEntity.status(401).body(response);
        }
    }

    @Operation(summary = "Gera um JWT", description = "Gera um token JWT com as claims fornecidas no corpo da requisição.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao processar a requisição")
    })
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateJwt(@RequestBody Map<String, Object> claims) {
        logger.info("Recebida solicitação para gerar JWT");

        Map<String, Object> response = new HashMap<>();

        try {
            String token = jwtService.generateToken(claims);
            response.put("status", "success");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao gerar JWT: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Erro ao gerar o token");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
