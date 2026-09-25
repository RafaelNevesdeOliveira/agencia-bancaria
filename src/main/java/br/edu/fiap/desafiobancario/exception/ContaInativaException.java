package br.edu.fiap.desafiobancario.exception;

/** Impede depósito ou saque em uma conta inativa. */
public class ContaInativaException extends ConflitoDeNegocioException {

    public ContaInativaException(Long id) {
        super("A conta " + id + " está inativa e não pode ser movimentada.");
    }
}

