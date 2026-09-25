package br.edu.fiap.desafiobancario.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Estrutura inicial para o teste unitário de {@link Pessoa}.
 *
 * <p>Crie uma pessoa e confirme que nome, CPF e e-mail recebidos pelo
 * construtor podem ser lidos pelos getters. Não inicie o Spring.</p>
 */
class PessoaTest {

    @Test
    @DisplayName("Deve preservar nome, CPF e e-mail informados ")
    void deveCriarPessoaComOsDadosInformados(){
        Pessoa pessoa = new Pessoa(
                "Mariana Silva",
                "12345612312",
                "mariana.silva@email.com"
        );

        assertThat(pessoa.getNome()).isEqualTo("Mariana Silva");
        assertThat(pessoa.getCpf()).isEqualTo("12345612312");
        assertThat(pessoa.getEmail()).isEqualTo("mariana.silva@email.com");
    }
}
