package br.edu.fiap.desafiobancario.service;

import br.edu.fiap.desafiobancario.dto.PessoaRequest;
import br.edu.fiap.desafiobancario.dto.PessoaResponse;
import br.edu.fiap.desafiobancario.entity.Pessoa;
import br.edu.fiap.desafiobancario.repository.PessoaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Estrutura inicial para os testes unitários de {@link PessoaService}.
 *
 * <p>O objeto testado deve ser o service real. Simule somente o repository com
 * Mockito. Para cada cenário, programe o mock com {@code when}, execute o
 * service, confira resultado ou exceção e use {@code verify} quando a interação
 * também fizer parte do comportamento esperado.</p>
 *
 * <p>Cenários mínimos: cadastrar CPF novo, recusar CPF duplicado, buscar pessoa
 * existente e informar pessoa inexistente.</p>
 */
@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    /** O Mockito cria o service real e injeta o mock pelo construtor. */
    @InjectMocks
    private PessoaService pessoaService;


    @Test
    @DisplayName("Deve cadastrar pessoa quando o CPF ainda não existe")
    void deveCadastrarPessoa() {
        PessoaRequest request = new PessoaRequest(
                "Mariana Costa", "12345678901", "mariana.costa@example.com");
        when(pessoaRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> {
            Pessoa salva = invocation.getArgument(0);
            // Em produção o PostgreSQL gera o ID; aqui simulamos esse efeito.
            ReflectionTestUtils.setField(salva, "id", 1L);
            return salva;
        });

        PessoaResponse response = pessoaService.cadastrar(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Mariana Costa");
        assertThat(response.cpf()).isEqualTo("12345678901");
        verify(pessoaRepository).save(any(Pessoa.class));
    }
}
