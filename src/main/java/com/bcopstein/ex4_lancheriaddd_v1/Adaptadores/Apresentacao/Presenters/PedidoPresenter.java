package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import java.util.List;

public class PedidoPresenter {

    public static class ItemPedidoPresenter {
        private final long   produtoId;
        private final String descricao;
        private final int    precoUnitario;   // em centavos
        private final int    quantidade;
        private final double subtotalItem;    // precoUnitario × quantidade

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
    private final String status;         // "APROVADO" ou "NEGADO"
    private final double valor;          // subtotal bruto dos itens
    private final double desconto;       // 7% se cliente fiel, senão 0
    private final double impostos;       // 10% sobre subtotal bruto
    private final double valorCobrado;   // (subtotal - desconto) + impostos
    private final boolean aprovado;      // true = aprovado, false = negado
    private final String mensagem;       // mensagem descritiva do resultado
    private final List<ItemPedidoPresenter> itens;

    public PedidoPresenter(long id, String status, double valor, double desconto,
                           double impostos, double valorCobrado, boolean aprovado,
                           String mensagem, List<ItemPedidoPresenter> itens) {
        this.id           = id;
        this.status       = status;
        this.valor        = valor;
        this.desconto     = desconto;
        this.impostos     = impostos;
        this.valorCobrado = valorCobrado;
        this.aprovado     = aprovado;
        this.mensagem     = mensagem;
        this.itens        = itens;
    }

    public long   getId()           { return id; }
    public String getStatus()       { return status; }
    public double getValor()        { return valor; }
    public double getDesconto()     { return desconto; }
    public double getImpostos()     { return impostos; }
    public double getValorCobrado() { return valorCobrado; }
    public boolean isAprovado()     { return aprovado; }
    public String getMensagem()     { return mensagem; }
    public List<ItemPedidoPresenter> getItens() { return itens; }
}
