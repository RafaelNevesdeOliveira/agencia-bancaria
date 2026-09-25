package br.edu.fiap.desafiobancario.service;


import br.edu.fiap.desafiobancario.dto.ContaBancariaRequest;
import br.edu.fiap.desafiobancario.dto.ContaBancariaResponse;
import br.edu.fiap.desafiobancario.dto.MovimentacaoRequest;
import br.edu.fiap.desafiobancario.entity.ContaBancaria;
import br.edu.fiap.desafiobancario.entity.Pessoa;
import br.edu.fiap.desafiobancario.entity.TipoConta;
import br.edu.fiap.desafiobancario.exception.ContaJaCadastradaException;
import br.edu.fiap.desafiobancario.exception.ContaNaoEncontradaException;
import br.edu.fiap.desafiobancario.exception.PessoaNaoEncontradaException;
import br.edu.fiap.desafiobancario.exception.TipoContaNaoEncontradoException;
import br.edu.fiap.desafiobancario.repository.ContaBancariaRepository;
import br.edu.fiap.desafiobancario.repository.PessoaRepository;
import br.edu.fiap.desafiobancario.repository.TipoContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço dos casos de uso bancários.
 *
 * <p>A classe coordena repositories diferentes e define os limites das
 * transações. A regra que altera o saldo continua na entidade
 * {@link ContaBancaria}, pois é ela que precisa proteger o próprio estado.</p>
 */
@Service
public class ContaBancariaService {

    private final ContaBancariaRepository contaRepository;
    private final PessoaRepository pessoaRepository;
    private final TipoContaRepository tipoContaRepository;

    /**
     * Injeta todas as portas de persistência usadas pelos casos de uso.
     *
     * @param contaRepository persistência de contas
     * @param pessoaRepository persistência de titulares
     * @param tipoContaRepository persistência dos tipos de conta
     */
    public ContaBancariaService(
            ContaBancariaRepository contaRepository,
            PessoaRepository pessoaRepository,
            TipoContaRepository tipoContaRepository) {
        this.contaRepository = contaRepository;
        this.pessoaRepository = pessoaRepository;
        this.tipoContaRepository = tipoContaRepository;
    }

    /**
     * Abre uma conta associada a uma pessoa e a um tipo existentes.
     *
     * @param request dados para abertura da conta
     * @return conta criada com os dados dos relacionamentos
     */
    @Transactional
    public ContaBancariaResponse abrir(ContaBancariaRequest request) {
        if (contaRepository.existsByAgenciaAndNumero(request.agencia(), request.numero())) {
            throw new ContaJaCadastradaException(request.agencia(), request.numero());
        }

        Pessoa titular = pessoaRepository.findById(request.titularId())
                .orElseThrow(() -> new PessoaNaoEncontradaException(request.titularId()));
        TipoConta tipoConta = tipoContaRepository.findById(request.tipoContaId())
                .orElseThrow(() -> new TipoContaNaoEncontradoException(request.tipoContaId()));

        ContaBancaria conta = new ContaBancaria(
                request.agencia(),
                request.numero(),
                request.saldoInicial(),
                request.ativa(),
                titular,
                tipoConta);
        return ContaBancariaResponse.de(contaRepository.save(conta));
    }

    /**
     * Consulta uma conta e monta o DTO ainda dentro da transação de leitura.
     * Isso permite acessar os relacionamentos lazy mesmo com open-in-view falso.
     *
     * @param id identificador da conta
     * @return conta e resumo dos relacionamentos
     */
    @Transactional(readOnly = true)
    public ContaBancariaResponse buscar(Long id) {
        return ContaBancariaResponse.de(buscarEntidade(id));
    }

    /**
     * Lista as contas associadas ao titular informado.
     *
     * @param pessoaId identificador do titular
     * @return contas pertencentes à pessoa
     */
    @Transactional(readOnly = true)
    public List<ContaBancariaResponse> listarPorPessoa(Long pessoaId) {
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new PessoaNaoEncontradaException(pessoaId);
        }

//        early return
        return contaRepository.findByTitularId(pessoaId).stream()
                .map(ContaBancariaResponse::de)
                .toList();
    }

    /**
     * Adiciona valor ao saldo da conta.
     *
     * <p>Não é necessário chamar {@code save} depois de alterar uma entidade
     * gerenciada. O dirty checking do JPA detecta a mudança e gera o UPDATE no
     * commit da transação.</p>
     *
     * @param contaId identificador da conta
     * @param request valor positivo do depósito
     * @return conta com o novo saldo
     */
    @Transactional
    public ContaBancariaResponse depositar(Long contaId, MovimentacaoRequest request) {
        ContaBancaria conta = buscarEntidade(contaId);
        conta.depositar(request.valor());
        return ContaBancariaResponse.de(conta);
    }

    /**
     * Retira valor sem permitir saldo negativo ou movimentação em conta inativa.
     * Qualquer exceção provoca rollback automático da transação.
     *
     * @param contaId identificador da conta
     * @param request valor positivo do saque
     * @return conta com o novo saldo
     */
    @Transactional
    public ContaBancariaResponse sacar(Long contaId, MovimentacaoRequest request) {
        ContaBancaria conta = buscarEntidade(contaId);
        conta.sacar(request.valor());
        return ContaBancariaResponse.de(conta);
    }

    /** Localiza a entidade ou traduz a ausência para uma exceção de domínio. */
    private ContaBancaria buscarEntidade(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }
}
