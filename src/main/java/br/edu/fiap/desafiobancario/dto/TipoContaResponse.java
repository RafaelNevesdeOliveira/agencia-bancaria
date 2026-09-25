package br.edu.fiap.desafiobancario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** JSON simples para representar um tipo de conta carregado pelo SQL. */
@Schema(
        name = "TipoContaResponse",
        description = "Representação pública de um tipo de conta disponível no banco.")
public record TipoContaResponse(
        @Schema(
                description = "Identificador único do tipo de conta.",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(
                description = "Nome do tipo de conta.",
                example = "Conta Corrente",
                accessMode = Schema.AccessMode.READ_ONLY)
        String nome) {
}
