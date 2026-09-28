package br.edu.fiap.desafiobancario.dto;


import br.edu.fiap.desafiobancario.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação pública de um usuário, sempre sem senha")
public record UsuarioResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Mariana Costa") String nome,
        @Schema(example = "mariana.costa@example.com") String email,
        @Schema(example = "true") boolean ativo) {
        public static UsuarioResponse de(Usuario usuario) {
            return new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.isAtivo());
        }
}
