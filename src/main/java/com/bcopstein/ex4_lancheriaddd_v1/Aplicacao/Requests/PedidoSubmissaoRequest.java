package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.util.List;

public class PedidoSubmissaoRequest {

    private String clienteCpf;
    private String enderecoEntrega;
    private List<ItemPedidoRequest> itens;

    public PedidoSubmissaoRequest() {}

    public PedidoSubmissaoRequest(String clienteCpf, String enderecoEntrega,
                                  List<ItemPedidoRequest> itens) {
        this.clienteCpf      = clienteCpf;
        this.enderecoEntrega = enderecoEntrega;
        this.itens           = itens;
    }

    public String getClienteCpf()               { return clienteCpf; }
    public String getEnderecoEntrega()           { return enderecoEntrega; }
    public List<ItemPedidoRequest> getItens()   { return itens; }

    public void setClienteCpf(String clienteCpf)            { this.clienteCpf = clienteCpf; }
    public void setEnderecoEntrega(String enderecoEntrega)  { this.enderecoEntrega = enderecoEntrega; }
    public void setItens(List<ItemPedidoRequest> itens)     { this.itens = itens; }
}
