package br.edu.fiap.desafiobancario.entity;

import br.edu.fiap.desafiobancario.exception.SaldoInsuficienteException;
import br.edu.fiap.desafiobancario.exception.ValorMovimentacaoInvalidoException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Estrutura inicial para os testes unitários de {@link ContaBancaria}.
 *
 * <p>Este arquivo não contém testes resolvidos. Implemente os cenários na ordem:</p>
 *
 * <ol>
 *   <li>depositar valor positivo em conta ativa;</li>
 *   <li>sacar quando houver saldo;</li>
 *   <li>recusar saque maior que o saldo;</li>
 *   <li>recusar movimentação em conta inativa;</li>
 *   <li>recusar valor zero ou negativo;</li>
 *   <li>recusar saldo inicial negativo.</li>
 * </ol>
 *
 * <p>Dica: use JUnit 5, AssertJ e a sequência Arrange, Act, Assert. Este é um
 * teste unitário puro: não use {@code @SpringBootTest} nem PostgreSQL.</p>
 */
class ContaBancariaTest {

    private Pessoa titular;
    private TipoConta tipoConta;


    @BeforeEach
    void prepararRelacionamento(){
        titular = new Pessoa("Filipe Videira", "11111111122", "filipe.videira@email.com");
        tipoConta =  new TipoConta("CORRENTE");
    }

    @Test
    @DisplayName("Deve somar o deposito ao saldo de uma conta ativa")
    void deveDepositarEmContaAtiva(){
        //Arrange
        ContaBancaria conta = novaConta("100000.00", true);

        //Act: executa um unico comportamento do dominio
        conta.depositar(new BigDecimal("500000.00"));

        //Assert: observa o resultado publicoda Operacapo
        assertThat(conta.getSaldo()).isEqualTo("600000.00");
    }

    @Test
    @DisplayName("Não deve permitir saque maior que o saldo")
    void naoDevePermitirSaldoNegativo(){
        ContaBancaria conta = novaConta("100.00", true);

        //ASSERT + ACT
        assertThatThrownBy(() -> conta.sacar(new BigDecimal("110.00")))
                .isInstanceOf(SaldoInsuficienteException.class)
                .hasMessageContaining("Saldo insuficiente");

        assertThat(conta.getSaldo()).isEqualByComparingTo("100.00");

    }

    @Test
    @DisplayName("Não deve aceitar movimentação com valor zero")
    void naoDeveAceitarValorZero() {
        ContaBancaria conta = novaConta("100.00", true);

        Assertions.assertThatThrownBy(() -> conta.depositar(BigDecimal.ZERO))
                .isInstanceOf(ValorMovimentacaoInvalidoException.class)
                .hasMessage("O valor da movimentação deve ser maior que zero.");
    }

    @Test
    @DisplayName("Não deve criar conta com saldo inicial negativo")
    void naoDeveCriarContaComSaldoNegativo() {
        Assertions.assertThatThrownBy(() -> novaConta("-0.01", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O saldo inicial não pode ser negativo.");
    }

    private ContaBancaria novaConta(String saldo, boolean ativa){
        return new ContaBancaria(
                "0001", "123456-7", new BigDecimal(saldo), ativa, titular, tipoConta
        );
    }


}
