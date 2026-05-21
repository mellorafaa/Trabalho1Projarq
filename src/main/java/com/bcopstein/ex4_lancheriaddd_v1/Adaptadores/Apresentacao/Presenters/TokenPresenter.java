package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.TokenResponse;

/**
 * Presenter que converte TokenResponse para formato de apresentação HTTP
 * Responsável pela transformação de dados de domínio para DTO
 */
public class TokenPresenter {

    private String accessToken;
    private String tipo;
    private long expiresIn;
    private String usuarioId;
    private String usuarioEmail;

    public TokenPresenter(TokenResponse response) {
        this.accessToken = response.getToken();
        this.tipo = response.getTipo();
        this.expiresIn = response.getExpiracaoMs();
        this.usuarioId = response.getUsuarioId();
        this.usuarioEmail = response.getUsuarioEmail();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTipo() {
        return tipo;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }
}
