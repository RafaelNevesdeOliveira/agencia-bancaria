package br.edu.fiap.desafiobancario.entity;

import br.edu.fiap.desafiobancario.exception.ContaInativaException;
import br.edu.fiap.desafiobancario.exception.SaldoInsuficienteException;
import br.edu.fiap.desafiobancario.exception.ValorMovimentacaoInvalidoException;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidade central do desafio. Guarda o saldo e as referências ao titular e ao
 * tipo da conta. As regras que protegem o próprio estado ficam nesta classe.
 */
@Entity
@Table(name = "contas_bancarias")
public class ContaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String agencia;

    @Column(nullable = false, length = 8)
    private String numero;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    private boolean ativa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "titular_id", nullable = false)
    private Pessoa titular;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_conta_id", nullable = false)
    private TipoConta tipoConta;

    protected ContaBancaria() {
        // Uso exclusivo do JPA.
    }

    public ContaBancaria(String agencia, String numero, BigDecimal saldoInicial,
                         boolean ativa, Pessoa titular, TipoConta tipoConta) {
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.agencia = agencia;
        this.numero = numero;
        this.saldo = saldoInicial;
        this.ativa = ativa;
        this.titular = titular;
        this.tipoConta = tipoConta;
    }

    /** Soma um valor positivo ao saldo da conta ativa. */
    public void depositar(BigDecimal valor) {
        validarContaAtiva();
        validarValor(valor);
        saldo = saldo.add(valor);
    }

    /** Subtrai um valor positivo sem permitir que o saldo fique negativo. */
    public void sacar(BigDecimal valor) {
        validarContaAtiva();
        validarValor(valor);
        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException(id, saldo, valor);
        }
        saldo = saldo.subtract(valor);
    }

    private void validarContaAtiva() {
        if (!ativa) {
            throw new ContaInativaException(id);
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorMovimentacaoInvalidoException();
        }
    }

    public Long getId() { return id; }
    public String getAgencia() { return agencia; }
    public String getNumero() { return numero; }
    public BigDecimal getSaldo() { return saldo; }
    public boolean isAtiva() { return ativa; }
    public Pessoa getTitular() { return titular; }
    public TipoConta getTipoConta() { return tipoConta; }
}
