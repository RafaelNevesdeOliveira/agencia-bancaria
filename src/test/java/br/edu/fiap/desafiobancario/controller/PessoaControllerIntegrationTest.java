package br.edu.fiap.desafiobancario.controller;

import br.edu.fiap.desafiobancario.dto.PessoaRequest;
import br.edu.fiap.desafiobancario.dto.PessoaResponse;
import br.edu.fiap.desafiobancario.exception.CpfJaCadastradoException;
import br.edu.fiap.desafiobancario.exception.GlobalExceptionHandler;
import br.edu.fiap.desafiobancario.service.PessoaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Estrutura inicial para o teste de integração da camada web de pessoa.
 *
 * <p>Use {@code @WebMvcTest(PessoaController.class)}, importe o handler global,
 * injete MockMvc e substitua PessoaService por {@code @MockitoBean}. Esse recorte
 * integra rota, JSON, Bean Validation e tratamento de erro, mas não usa banco.</p>
 *
 * <p>Cenários mínimos: cadastro com 201 e Location, entrada inválida com 400 e
 * CPF duplicado com 409.</p>
 */

@WebMvcTest(PessoaController.class)
@Import(GlobalExceptionHandler.class)
class PessoaControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private PessoaService pessoaService;

    @Test
    @DisplayName("POST /api/pessoas deve devolver 201 e location")
    void deveCadastrarPessoa() throws Exception{
        //ARRANGE
        PessoaResponse response = new PessoaResponse(
                1L, "Mariana", "11122233344", "pessoa@example.com");
        //ACT
        when(pessoaService.cadastrar(any(PessoaRequest.class))).thenReturn(response);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome":"Mariana Costa",
                                  "cpf":"12345678901",
                                  "email":"mariana.costa@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Mariana"));
    }


    //TODO
    @Test
    @DisplayName("POST /api/pessoas deve retornar CPF duplicado para 409")
    void deveRetornarConflitoCpf() throws Exception{
        //ACT
        when(pessoaService.cadastrar(any(PessoaRequest.class)))
                .thenThrow(new CpfJaCadastradoException("12345678901"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                  "nome":"Mariana Costa",
                                  "cpf":"12345678901",
                                  "email":"mariana.costa@example.com"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.mensagem").value("CPF já cadastrado: 12345678901"));
    }
}
