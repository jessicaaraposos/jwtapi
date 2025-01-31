package com.github.jessicaraposo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jessicaraposo.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JwtControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGenerateJwtSuccessfully() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "Admin");
        claims.put("Seed", 7);

        mockMvc.perform(post("/api/jwt/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claims)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldValidateJwtSuccessfully() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "User");
        claims.put("Role", "Admin");
        claims.put("Seed", 7);
        String token = jwtService.generateToken(claims);

        mockMvc.perform(post("/api/jwt/validate")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Token válido"));
    }

    @Test
    void shouldReturnBadRequestForInvalidTokenFormat() throws Exception {
        mockMvc.perform(post("/api/jwt/validate")
                        .header("Authorization", "InvalidToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Formato inválido do token. Use 'Bearer <token>'"));
    }

}
