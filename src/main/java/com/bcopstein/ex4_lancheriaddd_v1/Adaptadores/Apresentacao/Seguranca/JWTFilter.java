package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Seguranca;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter implements HandlerInterceptor {
    
    private final GeradorTokenJWT geradorTokenJWT;
    
    public JWTFilter(GeradorTokenJWT geradorTokenJWT) {
        this.geradorTokenJWT = geradorTokenJWT;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        // Rotas públicas não necessitam autenticação
        String path = request.getRequestURI();
        if (path.contains("/login") || path.contains("/health") || path.contains("/swagger")) {
            return true;
        }
        
        String authHeader = request.getHeader("Authorization");
        
        // Validar presença do header
        if (authHeader == null || authHeader.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"erro\": \"Token não encontrado\"}");
            return false;
        }
        
        // Extrair token do format "Bearer token"
        String token = authHeader.startsWith("Bearer ") 
            ? authHeader.substring(7) 
            : authHeader;
        
        // Validar token
        if (!geradorTokenJWT.isTokenValido(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"erro\": \"Token inválido ou expirado\"}");
            return false;
        }
        
        // Extrair usuário ID do token e adicionar ao request
        try {
            String usuarioId = geradorTokenJWT.validarEExtrairUsuarioId(token);
            request.setAttribute("usuarioId", usuarioId);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"erro\": \"Falha ao processar token\"}");
            return false;
        }
        
        return true;
    }
}
