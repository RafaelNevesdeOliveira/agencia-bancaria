package br.edu.fiap.desafiobancario.exception;

/** Falha usada quando o titular informado não existir. */
public class PessoaNaoEncontradaException extends RecursoNaoEncontradoException {

    public PessoaNaoEncontradaException(Long id) {
        super("Pessoa não encontrada: " + id);
    }
}

