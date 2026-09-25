package br.edu.fiap.desafiobancario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * JSON de entrada para cadastrar uma pessoa.
 *
 * <p>As validações impedem que dados estruturalmente inválidos cheguem ao
 * service. Regras como CPF duplicado continuarão sendo responsabilidade da
 * camada de negócio.</p>
 */
@Schema(
        name = "PessoaRequest",
        description = "Dados recebidos pela API para cadastrar uma pessoa titular de conta.")
public record PessoaRequest(
        @Schema(
                description = "Nome completo da pessoa.",
                example = "Ana Souza",
                minLength = 2,
                maxLength = 120,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome é obrigatório.")
        @Size(min = 2, max = 120, message = "Nome deve possuir entre 2 e 120 caracteres.")
        String nome,

        @Schema(
                description = "CPF com exatamente 11 algarismos, sem pontos ou traço.",
                example = "12345678901",
                pattern = "\\d{11}",
                minLength = 11,
                maxLength = 11,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "CPF deve possuir exatamente 11 números.")
        String cpf,

        @Schema(
                description = "Endereço de e-mail da pessoa.",
                example = "ana.souza@email.com",
                format = "email",
                maxLength = 160,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail deve possuir formato válido.")
        @Size(max = 160, message = "E-mail deve possuir no máximo 160 caracteres.")
        String email) {
}
