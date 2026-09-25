package br.edu.fiap.desafiobancario.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Converte exceções em respostas HTTP padronizadas.
 *
 * <p>O handler já está pronto para que os futuros services apenas lancem as
 * exceções corretas. Controllers não devem capturar essas falhas.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Converte recurso ausente em HTTP 404. */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> tratarNaoEncontrado(
            RecursoNaoEncontradoException erro,
            HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, erro.getMessage(), request, Map.of());
    }

    /** Converte uma regra de negócio violada em HTTP 409. */
    @ExceptionHandler(ConflitoDeNegocioException.class)
    public ResponseEntity<ApiErrorResponse> tratarConflito(
            ConflitoDeNegocioException erro,
            HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT, erro.getMessage(), request, Map.of());
    }

    /** Reúne os erros dos DTOs anotados com Bean Validation. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> tratarValidacao(
            MethodArgumentNotValidException erro,
            HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        erro.getBindingResult().getFieldErrors().forEach(fieldError ->
                campos.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Existem campos inválidos na requisição.",
                request,
                campos);
    }

    /** Converte JSON, ID ou valor incompatível em HTTP 400. */
    @ExceptionHandler({
            ValorMovimentacaoInvalidoException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiErrorResponse> tratarEntradaInvalida(
            Exception erro,
            HttpServletRequest request) {
        String mensagem = erro instanceof ValorMovimentacaoInvalidoException
                ? erro.getMessage()
                : "A requisição contém um valor ou formato inválido.";
        return resposta(HttpStatus.BAD_REQUEST, mensagem, request, Map.of());
    }

    /** Oculta detalhes internos quando ocorrer uma falha não prevista. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> tratarInesperado(
            Exception erro,
            HttpServletRequest request) {
        return resposta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno não esperado.",
                request,
                Map.of());
    }

    /** Monta o corpo comum das respostas de erro. */
    private ResponseEntity<ApiErrorResponse> resposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            Map<String, String> campos) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                campos);
        return ResponseEntity.status(status).body(response);
    }
}
