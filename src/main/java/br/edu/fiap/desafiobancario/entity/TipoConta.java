package br.edu.fiap.desafiobancario.entity;

import jakarta.persistence.*;

/**
 * Entidade de domínio que classifica uma conta como corrente, poupança ou salário.
 * Uma linha desta tabela pode ser referenciada por várias contas bancárias.
 */
@Entity
@Table(name = "tipos_conta")
public class TipoConta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String nome;

    protected TipoConta() {
        // Construtor exigido pelo JPA para reconstruir objetos vindos do banco.
    }

    public TipoConta(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
