package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.List;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoResponse {

    private final Pedido pedido;
    private final boolean aprovado;
    private final String mensagem;
    private final List<ItemPedido> itensIndisponiveis;

    public PedidoResponse(Pedido pedido, boolean aprovado, String mensagem,
                          List<ItemPedido> itensIndisponiveis) {
        this.pedido             = pedido;
        this.aprovado           = aprovado;
        this.mensagem           = mensagem;
        this.itensIndisponiveis = itensIndisponiveis;
    }

    public Pedido getPedido()                           { return pedido; }
    public boolean isAprovado()                         { return aprovado; }
    public String getMensagem()                         { return mensagem; }
    public List<ItemPedido> getItensIndisponiveis()     { return itensIndisponiveis; }
}
