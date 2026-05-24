package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Seguranca;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IGeradorToken;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class GeradorTokenJWT implements IGeradorToken {
    
    @Value("${jwt.secret:mySecretKeyThatIsVeryLongAndSecureForTokenGeneration12345678901234567890}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}") // 24 horas em ms
    private long jwtExpirationMs;
    
    
    public String gerarToken(Usuario usuario) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
            .subject(usuario.getId())
            .claim("email", usuario.getEmail())
            .claim("nome", usuario.getNome())
            .claim("role", usuario.getRole())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(key, Jwts.SIG.HS512)
            .compact();
    }
    
    
    public String validarEExtrairUsuarioId(String token) throws Exception {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (Exception e) {
            throw new Exception("Token JWT inválido: " + e.getMessage());
        }
    }
    
    
    public boolean isTokenValido(String token) {
        try {
            validarEExtrairUsuarioId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public long getTempoExpiracaoMs() {
        return jwtExpirationMs;
    }
}
