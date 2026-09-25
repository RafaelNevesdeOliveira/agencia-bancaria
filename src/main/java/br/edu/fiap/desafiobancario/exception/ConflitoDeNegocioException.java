package br.edu.fiap.desafiobancario.exception;

/** Classe-base das regras de negócio violadas que deverão produzir HTTP 409. */
public class ConflitoDeNegocioException extends RuntimeException {

    public ConflitoDeNegocioException(String mensagem) {
        super(mensagem);
    }
}

