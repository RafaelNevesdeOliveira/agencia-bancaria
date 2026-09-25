package br.edu.fiap.desafiobancario.exception;

/** Classe-base das falhas que deverão produzir HTTP 404. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

