package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public class TokenResponse {

    private final String token;
    private final String tipo;
    private final long expiracaoMs;
    private final String usuarioId;
    private final String usuarioEmail;

    public TokenResponse(String token, String tipo, long expiracaoMs,
                         String usuarioId, String usuarioEmail) {
        this.token = token;
        this.tipo = tipo;
        this.expiracaoMs = expiracaoMs;
        this.usuarioId = usuarioId;
        this.usuarioEmail = usuarioEmail;
    }

    public String getToken()        { return token; }
    public String getTipo()         { return tipo; }
    public long getExpiracaoMs()    { return expiracaoMs; }
    public String getUsuarioId()    { return usuarioId; }
    public String getUsuarioEmail() { return usuarioEmail; }
}
