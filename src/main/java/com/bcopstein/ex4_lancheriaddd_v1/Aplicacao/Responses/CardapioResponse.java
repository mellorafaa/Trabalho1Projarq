package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public class CardapioResponse {

    public record ItemCardapioDTO(long id, String descricao, int preco) {}

    private final long id;
    private final String titulo;
    private final List<ItemCardapioDTO> produtos;
    private final Set<Long> idsDoChef;

    public CardapioResponse(Cardapio cardapio, List<Produto> sugestoesDoChef) {
        this.id = cardapio.getCabecalhoCardapio().id();
        this.titulo = cardapio.getCabecalhoCardapio().titulo();
        this.produtos = cardapio.getProdutos().stream()
                .map(p -> new ItemCardapioDTO(p.getId(), p.getDescricao(), p.getPreco()))
                .toList();
        this.idsDoChef = sugestoesDoChef.stream()
                .map(Produto::getId)
                .collect(Collectors.toSet());
    }

    public long getId()                          { return id; }
    public String getTitulo()                    { return titulo; }
    public List<ItemCardapioDTO> getProdutos()   { return produtos; }
    public Set<Long> getIdsDoChef()              { return idsDoChef; }
}
