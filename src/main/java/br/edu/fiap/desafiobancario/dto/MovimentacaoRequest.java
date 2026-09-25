package br.edu.fiap.desafiobancario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** JSON compartilhado pelas futuras rotas de depósito e saque. */
@Schema(
        name = "MovimentacaoRequest",
        description = "Valor monetário recebido pelas operações de depósito e saque.")
public record MovimentacaoRequest(
        @Schema(
                description = "Valor da movimentação. Deve ser maior ou igual a 0,01.",
                example = "150.00",
                minimum = "0.01",
                format = "decimal",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Valor é obrigatório.")
        @DecimalMin(value = "0.01", message = "Valor deve ser pelo menos 0,01.")
        BigDecimal valor) {
}
