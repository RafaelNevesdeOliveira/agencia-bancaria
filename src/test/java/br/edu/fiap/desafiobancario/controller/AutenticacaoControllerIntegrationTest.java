package br.edu.fiap.desafiobancario.controller;


import br.edu.fiap.desafiobancario.dto.LoginRequest;
import br.edu.fiap.desafiobancario.dto.TokenResponse;
import br.edu.fiap.desafiobancario.exception.GlobalExceptionHandler;
import br.edu.fiap.desafiobancario.service.AutenticacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutenticacaoController.class)
@Import(GlobalExceptionHandler.class)
public class AutenticacaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticacaoService autenticacaoService;

    @Test
    @DisplayName("POST /api/auth/login deve devolver 200 com o JWT")
    void deveAutenticarEDevolverToken() throws Exception {
        //ACT
        when(autenticacaoService.login(any(LoginRequest.class))).thenReturn(
                new TokenResponse(
                        "header.payload.signature",
                        "Bearer",
                        900,
                        Instant.parse("2026-10-01T17:15:00Z")
                )
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "mariana@example.com",
                                  "senha": "Senha@123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("header.payload.signature"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(900))
                .andExpect(jsonPath("$.expiresAt").value("2026-10-01T17:15:00Z"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }


    @Test
    @DisplayName("POST /api/auth/login deve retornar 400 para credenciais não informadas")
    void deveValidarCamposDeLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                "email":"", "senha":""
                                                } 
                                                """
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.campos.email").exists())
                .andExpect(jsonPath("$.campos.senha").exists());

    }
}
