package br.edu.fiap.desafiobancario.service;

import br.edu.fiap.desafiobancario.dto.PessoaRequest;
import br.edu.fiap.desafiobancario.dto.PessoaResponse;
import br.edu.fiap.desafiobancario.entity.Pessoa;
import br.edu.fiap.desafiobancario.exception.CpfJaCadastradoException;
import br.edu.fiap.desafiobancario.exception.PessoaNaoEncontradaException;
import br.edu.fiap.desafiobancario.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Camada de serviço responsável pelos casos de uso de pessoa.
 *
 * <p>O controller conhece HTTP e o repository conhece persistência. Esta classe
 * fica entre os dois para coordenar o cadastro, aplicar a regra de CPF único e
 * controlar a transação sem misturar essas responsabilidades.</p>
 */
@Service
public class PessoaService {
    
    private final PessoaRepository pessoaRepository;

    /**
     * Recebe a dependência pelo construtor. O Spring localiza o repository e
     * fornece sua implementação automaticamente.
     *
     * @param pessoaRepository acesso aos dados de pessoa
     */
    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Cadastra um novo titular.
     *
     * <p>{@link Transactional} abre a transação antes do método. Se uma exceção
     * for lançada, o Spring executa rollback; se tudo terminar corretamente,
     * executa commit.</p>
     *
     * @param request dados já validados pelo controller
     * @return representação segura da pessoa cadastrada
     */
    @Transactional
    public PessoaResponse cadastrar(PessoaRequest request) {
        if (pessoaRepository.existsByCpf(request.cpf())) {
            throw new CpfJaCadastradoException(request.cpf());
        }


//        FAlSE
        Pessoa pessoa = new Pessoa(request.nome(), request.cpf(), request.email());
        return PessoaResponse.de(pessoaRepository.save(pessoa));
    }

    /**
     * Localiza uma pessoa para uso em outros casos de negócio.
     *
     * @param id identificador da pessoa
     * @return entidade encontrada
     * @throws PessoaNaoEncontradaException quando o ID não existe
     */
    @Transactional(readOnly = true)
    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException(id));
    }
}
