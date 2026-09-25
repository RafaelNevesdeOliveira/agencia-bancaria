package br.edu.fiap.desafiobancario.service;

import br.edu.fiap.desafiobancario.repository.PessoaRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    // TODO: adicionar MockitoExtension, @Mock e @InjectMocks.
    // TODO: implementar os quatro cenários descritos acima.
}
