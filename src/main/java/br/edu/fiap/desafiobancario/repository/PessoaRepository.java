package br.edu.fiap.desafiobancario.repository;

import br.edu.fiap.desafiobancario.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    /**
     * Verifica se o CPF já existe antes do cadastro.
     *
     * <p>O Spring Data interpreta {@code existsByCpf}: {@code exists} pede uma
     * verificação de existência e {@code Cpf} indica o atributo usado no filtro.</p>
     *
     * @param cpf CPF informado no cadastro
     * @return {@code true} quando já existe uma pessoa com o CPF
     */
    boolean existsByCpf(String cpf);
}
