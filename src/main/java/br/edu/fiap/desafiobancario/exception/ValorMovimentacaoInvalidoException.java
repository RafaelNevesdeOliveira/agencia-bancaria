package br.edu.fiap.desafiobancario.exception;

/** Representa depósito ou saque com valor nulo, zero ou negativo. */
public class ValorMovimentacaoInvalidoException extends IllegalArgumentException {

    public ValorMovimentacaoInvalidoException() {
        super("O valor da movimentação deve ser maior que zero.");
    }
}
