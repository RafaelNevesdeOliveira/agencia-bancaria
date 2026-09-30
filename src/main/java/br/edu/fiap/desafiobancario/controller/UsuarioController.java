package br.edu.fiap.desafiobancario.controller;


import br.edu.fiap.desafiobancario.dto.UsuarioRequest;
import br.edu.fiap.desafiobancario.dto.UsuarioResponse;
import br.edu.fiap.desafiobancario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


/**
 * Camada HTTP do cadastro de usuários.
 *
 * <p>As três rotas trabalham exclusivamente com DTOs. Essa fronteira impede
 * que a coluna de senha da entidade seja incluída nas respostas da API.</p>
 *
 * <p>{@code @SecurityRequirement(name = "bearerAuth")} está só em listar e
 * buscar. O cadastro permanece público na documentação, como na
 * {@code SecurityFilterChain}. Nos dois GETs, o Swagger liga a rota ao
 * esquema de {@code OpenApiConfig}: o cadeado aparece e, depois do
 * Authorize, o Try it out envia {@code Authorization: Bearer} com o
 * accessToken. A anotação só documenta o contrato.</p>
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Cadastro e consulta segura de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar usuário com senha protegida por BCrypt")
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.cadastrar(request);
        return ResponseEntity
                .created(URI.create("/api/usuarios/" + response.id()))
                .body(response);
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar usuários sem retornar senhas")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar usuário por ID sem retornar a senha")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

}
