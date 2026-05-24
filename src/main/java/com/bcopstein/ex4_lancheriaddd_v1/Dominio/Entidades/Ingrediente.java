package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Ingrediente {

    private final long id;
    private final String descricao;

    public Ingrediente(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public long getId()          { return id; }
    public String getDescricao() { return descricao; }
}
