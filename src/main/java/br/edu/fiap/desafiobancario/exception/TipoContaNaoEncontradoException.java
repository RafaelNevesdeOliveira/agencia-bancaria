package br.edu.fiap.desafiobancario.exception;

/** Falha usada quando o tipo de conta informado não existir. */
public class TipoContaNaoEncontradoException extends RecursoNaoEncontradoException {

    public TipoContaNaoEncontradoException(Long id) {
        super("Tipo de conta não encontrado: " + id);
    }
}

