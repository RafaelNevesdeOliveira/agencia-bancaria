package br.edu.fiap.desafiobancario.dto;

import br.edu.fiap.desafiobancario.entity.ContaBancaria;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * JSON de saída de uma conta e de seus relacionamentos.
 *
 * <p>O formato plano evita ciclos de serialização e não expõe proxies do JPA.</p>
 */
@Schema(
        name = "ContaBancariaResponse",
        description = "Representação pública e plana da conta bancária e de seus relacionamentos.")
public record ContaBancariaResponse(
        @Schema(description = "Identificador único da conta.", example = "10",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Código da agência.", example = "0001",
                accessMode = Schema.AccessMode.READ_ONLY)
        String agencia,

        @Schema(description = "Número da conta com dígito.", example = "123456-7",
                accessMode = Schema.AccessMode.READ_ONLY)
        String numero,

        @Schema(description = "Saldo disponível na conta.", example = "1250.75", format = "decimal",
                accessMode = Schema.AccessMode.READ_ONLY)
        BigDecimal saldo,

        @Schema(description = "Indica se a conta permite movimentações.", example = "true",
                accessMode = Schema.AccessMode.READ_ONLY)
        boolean ativa,

        @Schema(description = "Identificador da pessoa titular.", example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long titularId,

        @Schema(description = "Nome da pessoa titular.", example = "Ana Souza",
                accessMode = Schema.AccessMode.READ_ONLY)
        String titularNome,

        @Schema(description = "Identificador do tipo de conta.", example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long tipoContaId,

        @Schema(description = "Nome do tipo de conta.", example = "Conta Corrente",
                accessMode = Schema.AccessMode.READ_ONLY)
        String tipoContaNome) {

        public static ContaBancariaResponse de(ContaBancaria conta) {
                return new ContaBancariaResponse(
                        conta.getId(), conta.getAgencia(), conta.getNumero(), conta.getSaldo(), conta.isAtiva(),
                        conta.getTitular().getId(), conta.getTitular().getNome(),
                        conta.getTipoConta().getId(), conta.getTipoConta().getNome());
        }
}
