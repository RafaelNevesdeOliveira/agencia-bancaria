package br.edu.fiap.desafiobancario.exception;

import java.time.Instant;
import java.util.Map;

/** Contrato padronizado usado por todas as respostas de erro. */
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos) {
}

