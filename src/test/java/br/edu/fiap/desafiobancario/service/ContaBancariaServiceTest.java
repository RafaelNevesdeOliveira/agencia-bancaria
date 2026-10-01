package br.edu.fiap.desafiobancario.service;

import br.edu.fiap.desafiobancario.dto.ContaBancariaRequest;
import br.edu.fiap.desafiobancario.dto.ContaBancariaResponse;
import br.edu.fiap.desafiobancario.dto.MovimentacaoRequest;
import br.edu.fiap.desafiobancario.entity.ContaBancaria;
import br.edu.fiap.desafiobancario.entity.Pessoa;
import br.edu.fiap.desafiobancario.entity.TipoConta;
import br.edu.fiap.desafiobancario.exception.ContaJaCadastradaException;
import br.edu.fiap.desafiobancario.exception.SaldoInsuficienteException;
import br.edu.fiap.desafiobancario.repository.ContaBancariaRepository;
import br.edu.fiap.desafiobancario.repository.PessoaRepository;
import br.edu.fiap.desafiobancario.repository.TipoContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * Estrutura inicial para os testes unitários de {@link ContaBancariaService}.
 *
 * <p>Use o service real e mocks de ContaBancariaRepository,
 * PessoaRepository e TipoContaRepository. Nenhum teste desta classe deve abrir
 * conexão com o PostgreSQL.</p>
 *
 * <p>Cenários mínimos: abrir uma conta válida, recusar agência/número
 * duplicados, depositar por meio da entidade e recusar saque sem saldo.</p>
 */
@ExtendWith(MockitoExtension.class)
class ContaBancariaServiceTest {
    @Mock private ContaBancariaRepository contaBancariaRepository;
    @Mock private PessoaRepository pessoaRepository;
    @Mock private TipoContaRepository tipoContaRepository;
    @InjectMocks private ContaBancariaService contaBancariaService;

    private Pessoa titular;
    private TipoConta tipoConta;

    @BeforeEach
    void prepararRelacionamentos(){
        titular = new Pessoa("Faustão", "33344422299", "fausto@exemplo.com");
        tipoConta = new TipoConta("CORRENTE");
        ReflectionTestUtils.setField(titular, "id", 1L);
        ReflectionTestUtils.setField(tipoConta, "id", 2L);
    }

    @Test
    @DisplayName("Deve depositar usando a regra da entidade")
    void deveDepositar(){
        //ARRANGE
        ContaBancaria conta = novaConta("100.00", true);
        //ACT
        when(contaBancariaRepository.findById(10L)).thenReturn(Optional.of(conta));

        ContaBancariaResponse response = contaBancariaService
                .depositar(10L, new MovimentacaoRequest(new BigDecimal("25.00")));

        //ASSERT
        assertThat(response.saldo()).isEqualByComparingTo("125.00");

        verify(contaBancariaRepository, never()).save(any(ContaBancaria.class));
    }

    @Test
    @DisplayName("Deve propagar a falha quando o saque supera o saldo")
    void deveFalharQuandoSaldoInsuficiente(){
        //ARRANGE
        ContaBancaria conta = novaConta("1.00", true);

        //ACT
        when(contaBancariaRepository.findById(10L)).thenReturn(Optional.of(conta));

        //ASSERT
        assertThatThrownBy(() -> contaBancariaService.sacar(
                10L, new MovimentacaoRequest(new BigDecimal("20"))))
                .isInstanceOf(SaldoInsuficienteException.class);

        assertThat(conta.getSaldo()).isEqualByComparingTo("1.00");
    }

    //TODO
    @Test
    @DisplayName("Deve interromper a abertura quando a conta já existe")
    void naoDeveAbrirContaDuplicada(){
        //ARRANGE
        ContaBancariaRequest request = new ContaBancariaRequest(
                "0001", "123456-7", BigDecimal.ZERO, true, 1L, 2L);

        //ACT
        when(contaBancariaRepository
                .existsByAgenciaAndNumero("0001", "123456-7")).thenReturn(true);

        //ASSERT
        assertThatThrownBy(()-> contaBancariaService.abrir(request))
                .isInstanceOf(ContaJaCadastradaException.class)
                        .hasMessageContaining("123456-7");


        verify(pessoaRepository, never()).findAllById(any());
        verify(tipoContaRepository, never()).findAllById(any());
        verify(contaBancariaRepository, never()).findAllById(any());

    }


    private ContaBancaria novaConta(String saldo, boolean ativa){
        ContaBancaria conta = new ContaBancaria(
                "0001",
                "123456-7",
                new BigDecimal(saldo),
                ativa,
                titular,
                tipoConta);
        ReflectionTestUtils.setField(conta, "id", 10L);
        return conta;
    }
}
