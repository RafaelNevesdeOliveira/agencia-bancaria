package br.edu.fiap.desafiobancario.repository;

import br.edu.fiap.desafiobancario.entity.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Camada de persistência das contas bancárias.
 *
 * <p>Esta interface oferece o CRUD herdado e duas consultas derivadas. Os nomes
 * dos métodos funcionam como uma pequena linguagem: o Spring Data lê os nomes,
 * encontra os atributos da entidade e gera as consultas JPA automaticamente.</p>
 */
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

    /**
     * Protege a chave de negócio formada por agência e número.
     *
     * @param agencia agência informada
     * @param numero número informado
     * @return {@code true} quando a combinação já estiver cadastrada
     */
    boolean existsByAgenciaAndNumero(String agencia, String numero);

    /**
     * Busca todas as contas cujo relacionamento {@code titular.id} corresponde
     * ao ID recebido. Em {@code findByTitularId}, o Spring Data percorre o
     * atributo {@code titular} e usa seu atributo {@code id} no filtro.
     *
     * @param titularId ID da pessoa titular
     * @return contas relacionadas à pessoa
     */
    //pessoa
    List<ContaBancaria> findByTitularId(Long titularId);

    //JOIN
}
