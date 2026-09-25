package br.edu.fiap.desafiobancario.exception;

/** Impede o cadastro de duas pessoas com o mesmo CPF. */
public class CpfJaCadastradoException extends ConflitoDeNegocioException {

    public CpfJaCadastradoException(String cpf) {
        super("CPF já cadastrado: " + cpf);
    }
}

