package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

/**
 * Response contendo o token JWT após autenticação bem-sucedida
 */
public class TokenResponse {
    private String token;
    private String tipo;
    private long expiracaoMs;
    private String usuarioId;
    private String usuarioEmail;

    public TokenResponse() {
    }

    public TokenResponse(String token, String tipo, long expiracaoMs, String usuarioId, String usuarioEmail) {
        this.token = token;
        this.tipo = tipo;
        this.expiracaoMs = expiracaoMs;
        this.usuarioId = usuarioId;
        this.usuarioEmail = usuarioEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getExpiracaoMs() {
        return expiracaoMs;
    }

    public void setExpiracaoMs(long expiracaoMs) {
        this.expiracaoMs = expiracaoMs;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }
}
