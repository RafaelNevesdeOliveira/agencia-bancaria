package br.edu.fiap.desafiobancario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "JWT emitido depois que a senha BCrypt foi validada")
public record TokenResponse(
        @Schema(description = "JWT assinado no formato header.payload.signature")
        String accessToken,

        @Schema(example = "Bearer")
        String tokenType,

        @Schema(example = "900", description = "Tempo de vida do token em segundos")
        long expiresIn,

        @Schema(description = "Instante UTC em que o token deixa de ser aceito")
        Instant expiresAt) {
}
