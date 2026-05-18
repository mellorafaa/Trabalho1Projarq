package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public class RegistrarClienteResponse {
    private final boolean sucesso;
    private final String mensagem;

    public RegistrarClienteResponse(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
}
