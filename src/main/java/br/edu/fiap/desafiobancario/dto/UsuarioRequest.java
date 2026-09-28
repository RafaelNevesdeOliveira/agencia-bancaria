package br.edu.fiap.desafiobancario.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para cadastrar um usuário")
public record UsuarioRequest(
        @NotBlank(message = "Nome é obrigatório.")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres.")
        @Schema(example = "Mariana Costa")
        String nome,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail deve ter formato válido.")
        @Size(max = 160, message = "E-mail deve ter no máximo 160 caracteres.")
        @Schema(example = "mariana.costa@example.com")
        String email,

        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 8, max = 72,
                message = "Senha deve ter entre 8 e 72 caracteres.")
        @Schema(
                example = "Senha@123",
                accessMode = Schema.AccessMode.WRITE_ONLY,
                description = "Recebida somente no cadastro; nunca volta na resposta")
        String senha
) {
}
