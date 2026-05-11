package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoResponse {

    private final Pedido pedido;

    private final boolean aprovado;

    private final String mensagem;

    public PedidoResponse(Pedido pedido, boolean aprovado, String mensagem) {
        this.pedido   = pedido;
        this.aprovado = aprovado;
        this.mensagem = mensagem;
    }

    public Pedido getPedido()   { return pedido; }
    public boolean isAprovado() { return aprovado; }
    public String getMensagem() { return mensagem; }
}
