package br.edu.fiap.desafiobancario.exception;

/** Falha usada quando a conta bancária informada não existir. */
public class ContaNaoEncontradaException extends RecursoNaoEncontradoException {

    public ContaNaoEncontradaException(Long id) {
        super("Conta bancária não encontrada: " + id);
    }
}

