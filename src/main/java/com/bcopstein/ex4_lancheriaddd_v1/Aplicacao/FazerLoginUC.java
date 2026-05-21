package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.TokenResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Seguranca.GeradorTokenJWT;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.AutenticacaoServico;

/**
 * Use Case de aplicação para fazer login
 * Responsável pela orquestração do fluxo:
 * 1. Validar dados de entrada
 * 2. Autenticar o usuário
 * 3. Gerar token JWT
 * 4. Retornar token
 */
@Component
public class FazerLoginUC {
    
    private final AutenticacaoServico autenticacaoServico;
    private final GeradorTokenJWT geradorTokenJWT;
    
    @Autowired
    public FazerLoginUC(AutenticacaoServico autenticacaoServico, GeradorTokenJWT geradorTokenJWT) {
        this.autenticacaoServico = autenticacaoServico;
        this.geradorTokenJWT = geradorTokenJWT;
    }
    
    
    public TokenResponse executar(LoginRequest request) {
        // Validar entrada
        if (request == null || request.getEmail() == null || request.getSenha() == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }
        
        // Autenticar usuário
        Usuario usuario = autenticacaoServico.autenticar(request.getEmail(), request.getSenha());
        
        // Gerar token
        String token = geradorTokenJWT.gerarToken(usuario);
        long expiracaoMs = geradorTokenJWT.getTempoExpiracaoMs();
        
        // Retornar response
        return new TokenResponse(
            token,
            "Bearer",
            expiracaoMs,
            usuario.getId(),
            usuario.getEmail()
        );
    }
}
