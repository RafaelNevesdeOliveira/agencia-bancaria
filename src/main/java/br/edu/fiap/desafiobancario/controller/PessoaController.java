package br.edu.fiap.desafiobancario.controller;

import br.edu.fiap.desafiobancario.dto.PessoaRequest;
import br.edu.fiap.desafiobancario.dto.PessoaResponse;
import br.edu.fiap.desafiobancario.service.PessoaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Camada HTTP do recurso pessoa.
 *
 * <p>O controller traduz HTTP para uma chamada de serviço: recebe JSON,
 * solicita a validação do DTO, delega o caso de uso e escolhe o status da
 * resposta. Regras de CPF e persistência não pertencem a esta classe.</p>
 */
@RestController
@RequestMapping("/api/pessoas")
@Tag(name = "Pessoas")
@SecurityRequirement(name = "bearerAuth")
public class PessoaController {

    private final PessoaService pessoaService;

    /** @param pessoaService serviço que executa o cadastro */
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    /**
     * Cadastra um titular e devolve HTTP 201.
     *
     * @param request JSON validado pelo Bean Validation
     * @return pessoa criada e endereço do novo recurso
     */
    @PostMapping
    public ResponseEntity<PessoaResponse> cadastrar(
            @Valid @RequestBody PessoaRequest request) {
        PessoaResponse response = pessoaService.cadastrar(request);
        return ResponseEntity
                .created(URI.create("/api/pessoas/" + response.id()))
                .body(response);
    }
}
