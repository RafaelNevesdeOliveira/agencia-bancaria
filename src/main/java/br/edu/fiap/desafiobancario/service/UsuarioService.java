package br.edu.fiap.desafiobancario.service;

import br.edu.fiap.desafiobancario.dto.UsuarioRequest;
import br.edu.fiap.desafiobancario.dto.UsuarioResponse;
import br.edu.fiap.desafiobancario.entity.Usuario;
import br.edu.fiap.desafiobancario.exception.EmailJaCadastradoException;
import br.edu.fiap.desafiobancario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new EmailJaCadastradoException(request.email());
        }

        String senhaHash = passwordEncoder.encode(request.senha());
        Usuario usuario = new Usuario(request.nome(), request.email(), senhaHash);
        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }
}

//Entrada dados execucao do servico
//Salt BCrypt gera um valor aleatorio diferente para cada execucao --> uma string
//Custo: