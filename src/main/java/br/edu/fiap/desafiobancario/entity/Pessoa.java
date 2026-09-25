package br.edu.fiap.desafiobancario.entity;

import jakarta.persistence.*;

/**
 * Entidade que representa o titular cadastrado na agência.
 * CPF identifica a pessoa no negócio; o ID identifica a linha no banco.
 */
@Entity
@Table(name = "pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false,unique = true, length = 160)
    private String email;

    protected Pessoa() {
        // Uso exclusivo do JPA.
    }

    public Pessoa(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }
}
