package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class SolicitacaoItem {

    private final long produtoId;
    private final int quantidade;

    public SolicitacaoItem(long produtoId, int quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public long getProdutoId() { return produtoId; }
    public int getQuantidade() { return quantidade; }
}
