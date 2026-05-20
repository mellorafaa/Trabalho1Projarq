package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.util.List;

public class PedidoSubmissaoRequest {

    private String clienteCpf;

    private List<ItemPedidoRequest> itens;

    public PedidoSubmissaoRequest() {}

    public PedidoSubmissaoRequest(String clienteCpf, List<ItemPedidoRequest> itens) {
        this.clienteCpf = clienteCpf;
        this.itens = itens;
    }

    public String getClienteCpf()              { return clienteCpf; }
    public List<ItemPedidoRequest> getItens()  { return itens; }

    public void setClienteCpf(String clienteCpf)           { this.clienteCpf = clienteCpf; }
    public void setItens(List<ItemPedidoRequest> itens)    { this.itens = itens; }
}
