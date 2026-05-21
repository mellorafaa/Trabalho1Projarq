package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;


@Service
public class AutenticacaoServico {
    
    private final UsuarioRepository usuarioRepository;
    private final CriptografiaSenhaServico criptografiaSenhaServico;
    
    public AutenticacaoServico(UsuarioRepository usuarioRepository, 
                               CriptografiaSenhaServico criptografiaSenhaServico) {
        this.usuarioRepository = usuarioRepository;
        this.criptografiaSenhaServico = criptografiaSenhaServico;
    }
    
    public Usuario autenticar(String email, String senha) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode estar vazio");
        }
        
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha não pode estar vazia");
        }
        
        Usuario usuario = usuarioRepository.recuperarPorEmail(email);
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        
        if (!usuario.estaAtivo()) {
            throw new IllegalArgumentException("Usuário inativo");
        }
        
        if (!criptografiaSenhaServico.verificar(senha, usuario.getSenhaHash())) {
            throw new IllegalArgumentException("Senha incorreta");
        }
        
        return usuario;
    }
    
    public Usuario recuperarUsuario(String id) {
        Usuario usuario = usuarioRepository.recuperarPorId(id);
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        
        return usuario;
    }
}
