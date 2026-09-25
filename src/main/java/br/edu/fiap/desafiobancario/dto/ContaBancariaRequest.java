package br.edu.fiap.desafiobancario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * JSON de entrada para abrir uma conta bancária.
 *
 * <p>{@code titularId} e {@code tipoContaId} deverão ser usados pelo service
 * para localizar as entidades relacionadas antes de salvar a conta.</p>
 */
@Schema(
        name = "ContaBancariaRequest",
        description = "Dados necessários para abrir uma conta e associá-la a uma pessoa e a um tipo de conta.")
public record ContaBancariaRequest(
        @Schema(
                description = "Código da agência com quatro algarismos.",
                example = "0001",
                pattern = "\\d{4}",
                minLength = 4,
                maxLength = 4,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Agência é obrigatória.")
        @Pattern(regexp = "\\d{4}", message = "Agência deve possuir 4 números.")
        String agencia,

        @Schema(
                description = "Número da conta no formato de seis algarismos, hífen e dígito.",
                example = "123456-7",
                pattern = "\\d{6}-\\d",
                minLength = 8,
                maxLength = 8,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Número é obrigatório.")
        @Pattern(regexp = "\\d{6}-\\d", message = "Número deve seguir o formato 123456-7.")
        String numero,

        @Schema(
                description = "Saldo disponível no momento da abertura. Pode ser zero, mas não negativo.",
                example = "500.00",
                minimum = "0.00",
                format = "decimal",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Saldo inicial é obrigatório.")
        @DecimalMin(value = "0.00", message = "Saldo inicial não pode ser negativo.")
        BigDecimal saldoInicial,

        @Schema(
                description = "Indica se a conta deve ser aberta ativa.",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Situação ativa é obrigatória.")
        Boolean ativa,

        @Schema(
                description = "Identificador da pessoa que será titular da conta.",
                example = "1",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Titular é obrigatório.")
        @Positive(message = "ID do titular deve ser positivo.")
        Long titularId,

        @Schema(
                description = "Identificador do tipo de conta previamente cadastrado.",
                example = "1",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Tipo de conta é obrigatório.")
        @Positive(message = "ID do tipo de conta deve ser positivo.")
        Long tipoContaId) {
}
