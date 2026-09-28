package br.edu.fiap.desafiobancario.exception;

/** Erro 404 usado quando o usuário solicitado não existe. */
public class UsuarioNaoEncontradoException extends RecursoNaoEncontradoException {
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado: " + id);
    }
}
