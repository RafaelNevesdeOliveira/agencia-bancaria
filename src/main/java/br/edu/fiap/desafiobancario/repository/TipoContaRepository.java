package br.edu.fiap.desafiobancario.repository;

import br.edu.fiap.desafiobancario.entity.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Camada de persistência dos tipos de conta carregados pelo SQL.
 *
 * <p>Nenhum método adicional é necessário: {@code findById}, herdado de
 * {@link JpaRepository}, é suficiente para validar o tipo durante a abertura
 * de uma conta.</p>
 */
public interface TipoContaRepository extends JpaRepository<TipoConta, Long> {
}
