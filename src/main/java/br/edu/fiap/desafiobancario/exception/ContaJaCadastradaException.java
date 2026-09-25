package br.edu.fiap.desafiobancario.exception;

/** Protege a chave de negócio formada por agência e número. */
public class ContaJaCadastradaException extends ConflitoDeNegocioException {

    public ContaJaCadastradaException(String agencia, String numero) {
        super("Conta já cadastrada para agência " + agencia + " e número " + numero);
    }
}

