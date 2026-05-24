package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class ItemPedido {

    private final Produto item;
    private final int quantidade;

    public ItemPedido(Produto item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    public Produto getItem()     { return item; }
    public int getQuantidade()   { return quantidade; }
}
