package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.TokenResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.AutenticacaoServico;

@Component
public class FazerLoginUC {
    
    private final AutenticacaoServico autenticacaoServico;
    private final IGeradorToken geradorToken;
    
    public FazerLoginUC(AutenticacaoServico autenticacaoServico, IGeradorToken geradorToken) {
        this.autenticacaoServico = autenticacaoServico;
        this.geradorToken = geradorToken;
    }
    
    
    public TokenResponse executar(LoginRequest request) {
        // Validar entrada
        if (request == null || request.getEmail() == null || request.getSenha() == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }
        
        // Autenticar usuário
        Usuario usuario = autenticacaoServico.autenticar(request.getEmail(), request.getSenha());
        
        // Gerar token
        String token = geradorToken.gerarToken(usuario);
        long expiracaoMs = geradorToken.getTempoExpiracaoMs();
        
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
