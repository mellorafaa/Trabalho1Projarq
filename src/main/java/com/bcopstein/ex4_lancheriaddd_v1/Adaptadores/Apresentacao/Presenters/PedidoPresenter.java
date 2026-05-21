package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import java.util.List;

public class PedidoPresenter {

    public static class ItemPedidoPresenter {
        private final long   produtoId;
        private final String descricao;
        private final int    precoUnitario;
        private final int    quantidade;
        private final double subtotalItem;

        public ItemPedidoPresenter(long produtoId, String descricao,
                                   int precoUnitario, int quantidade) {
            this.produtoId     = produtoId;
            this.descricao     = descricao;
            this.precoUnitario = precoUnitario;
            this.quantidade    = quantidade;
            this.subtotalItem  = (double) precoUnitario * quantidade;
        }

        public long   getProdutoId()     { return produtoId; }
        public String getDescricao()     { return descricao; }
        public int    getPrecoUnitario() { return precoUnitario; }
        public int    getQuantidade()    { return quantidade; }
        public double getSubtotalItem()  { return subtotalItem; }
    }

    private final long   id;
    private final String status;
    private final double valor;
    private final double desconto;
    private final double impostos;
    private final double valorCobrado;
    private final boolean aprovado;
    private final String mensagem;
    private final String enderecoEntrega;
    private final List<ItemPedidoPresenter> itens;
    private final List<ItemPedidoPresenter> itensIndisponiveis;

    public PedidoPresenter(long id, String status, double valor, double desconto,
                           double impostos, double valorCobrado, boolean aprovado,
                           String mensagem, String enderecoEntrega,
                           List<ItemPedidoPresenter> itens,
                           List<ItemPedidoPresenter> itensIndisponiveis) {
        this.id                 = id;
        this.status             = status;
        this.valor              = valor;
        this.desconto           = desconto;
        this.impostos           = impostos;
        this.valorCobrado       = valorCobrado;
        this.aprovado           = aprovado;
        this.mensagem           = mensagem;
        this.enderecoEntrega    = enderecoEntrega;
        this.itens              = itens;
        this.itensIndisponiveis = itensIndisponiveis;
    }

    public long   getId()                                       { return id; }
    public String getStatus()                                   { return status; }
    public double getValor()                                    { return valor; }
    public double getDesconto()                                 { return desconto; }
    public double getImpostos()                                 { return impostos; }
    public double getValorCobrado()                             { return valorCobrado; }
    public boolean isAprovado()                                 { return aprovado; }
    public String getMensagem()                                 { return mensagem; }
    public String getEnderecoEntrega()                          { return enderecoEntrega; }
    public List<ItemPedidoPresenter> getItens()                 { return itens; }
    public List<ItemPedidoPresenter> getItensIndisponiveis()    { return itensIndisponiveis; }
}
