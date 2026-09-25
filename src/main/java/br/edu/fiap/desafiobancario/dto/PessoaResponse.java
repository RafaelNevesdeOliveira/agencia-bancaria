package br.edu.fiap.desafiobancario.dto;

import br.edu.fiap.desafiobancario.entity.Pessoa;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JSON de saída da pessoa.
 *
 * <p>O DTO separa o contrato HTTP da futura entidade JPA. O aluno deverá criar
 * a conversão da entidade para este formato no service ou em um método fábrica.</p>
 */
@Schema(
        name = "PessoaResponse",
        description = "Representação pública de uma pessoa cadastrada.")
public record PessoaResponse(
        @Schema(
                description = "Identificador único gerado pelo banco.",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(
                description = "Nome completo da pessoa.",
                example = "Ana Souza",
                accessMode = Schema.AccessMode.READ_ONLY)
        String nome,

        @Schema(
                description = "CPF com 11 algarismos.",
                example = "12345678901",
                accessMode = Schema.AccessMode.READ_ONLY)
        String cpf,

        @Schema(
                description = "Endereço de e-mail da pessoa.",
                example = "ana.souza@email.com",
                format = "email",
                accessMode = Schema.AccessMode.READ_ONLY)
        String email) {

        public static PessoaResponse de(Pessoa pessoa) {
                return new PessoaResponse(pessoa.getId(), pessoa.getNome(), pessoa.getCpf(), pessoa.getEmail());
        }
}
