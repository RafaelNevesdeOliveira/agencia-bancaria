package br.edu.fiap.desafiobancario.controller;

import br.edu.fiap.desafiobancario.dto.ContaBancariaRequest;
import br.edu.fiap.desafiobancario.dto.ContaBancariaResponse;
import br.edu.fiap.desafiobancario.dto.MovimentacaoRequest;
import br.edu.fiap.desafiobancario.service.ContaBancariaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Camada HTTP das contas bancárias.
 *
 * <p>Cada método representa uma rota e delega imediatamente para o service.
 * O controller não calcula saldo e não acessa repositories, mantendo HTTP,
 * transação e regra de negócio em camadas separadas.</p>
 *
 * <p>{@code @SecurityRequirement(name = "bearerAuth")} fica na classe, então
 * vale para abrir, consultar, listar, depositar e sacar. No Swagger ela
 * liga cada rota ao esquema de {@code OpenApiConfig}: o cadeado aparece e,
 * depois do Authorize, o Try it out envia {@code Authorization: Bearer}
 * com o accessToken. A anotação só documenta o contrato. Quem recusa a
 * chamada sem JWT válido é o {@code SecurityFilterChain}.</p>
 */
@RestController
@RequestMapping("/api/contas")
@Tag(name = "Contas bancárias")
@SecurityRequirement(name = "bearerAuth")
public class ContaBancariaController {

    private final ContaBancariaService contaService;

    /** @param contaService serviço com os casos de uso bancários */
    public ContaBancariaController(ContaBancariaService contaService) {
        this.contaService = contaService;
    }

    /** Abre uma conta relacionada e devolve HTTP 201. */
    @PostMapping
    public ResponseEntity<ContaBancariaResponse> abrir(
            @Valid @RequestBody ContaBancariaRequest request) {
        ContaBancariaResponse response = contaService.abrir(request);
        return ResponseEntity
                .created(URI.create("/api/contas/" + response.id()))
                .body(response);
    }

    /** Consulta uma conta e os dois relacionamentos pelo ID. */
    @GetMapping("/{id}")
    public ResponseEntity<ContaBancariaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.buscar(id));
    }

    /** Lista somente as contas pertencentes ao titular informado. */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<ContaBancariaResponse>> listarPorPessoa(
            @PathVariable Long pessoaId) {
        return ResponseEntity.ok(contaService.listarPorPessoa(pessoaId));
    }

    /** Soma um valor positivo ao saldo da conta. */
    @PatchMapping("/{id}/depositos")
    public ResponseEntity<ContaBancariaResponse> depositar(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoRequest request) {
        return ResponseEntity.ok(contaService.depositar(id, request));
    }

    /** Subtrai um valor positivo sem permitir saldo negativo. */
    @PatchMapping("/{id}/saques")
    public ResponseEntity<ContaBancariaResponse> sacar(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoRequest request) {
        return ResponseEntity.ok(contaService.sacar(id, request));
    }
}