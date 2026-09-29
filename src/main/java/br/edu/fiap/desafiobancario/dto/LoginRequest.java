package br.edu.fiap.desafiobancario.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais enviadas para autenticar um usuário")
public record LoginRequest(
        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail deve ter formato válido.")
        @Schema(example = "mariana.usuario@example.com")
        String email,

        @NotBlank(message = "Senha é obrigatória.")
        @Schema(
                example = "Senha@123",
                accessMode = Schema.AccessMode.WRITE_ONLY,
                description = "Usada somente pelo PasswordEncoder.matches; nunca entra no JWT")
        String senha
        ) {
}
