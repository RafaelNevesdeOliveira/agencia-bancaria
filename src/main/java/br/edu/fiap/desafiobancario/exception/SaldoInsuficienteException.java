package br.edu.fiap.desafiobancario.exception;

import java.math.BigDecimal;

/** Impede que um saque deixe o saldo negativo. */
public class SaldoInsuficienteException extends ConflitoDeNegocioException {

    public SaldoInsuficienteException(Long id, BigDecimal saldo, BigDecimal valor) {
        super("Saldo insuficiente na conta " + id + ". Saldo: " + saldo + ", saque: " + valor);
    }
}

