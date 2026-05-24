package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.List;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoResponse {

    public record ItemDTO(long produtoId, String descricao, int precoUnitario, int quantidade) {}

    public record PedidoDTO(
            long id,
            String status,
            double valor,
            double desconto,
            double impostos,
            double valorCobrado,
            String enderecoEntrega,
            List<ItemDTO> itens) {}

    private final PedidoDTO pedido;
    private final boolean aprovado;
    private final String mensagem;
    private final List<ItemDTO> itensIndisponiveis;

    public PedidoResponse(Pedido pedido, boolean aprovado, String mensagem,
                          List<ItemPedido> itensIndisponiveis) {
        this.pedido = pedido != null ? toPedidoDTO(pedido) : null;
        this.aprovado = aprovado;
        this.mensagem = mensagem;
        this.itensIndisponiveis = itensIndisponiveis.stream()
                .map(PedidoResponse::toItemDTO).toList();
    }

    private static PedidoDTO toPedidoDTO(Pedido p) {
        List<ItemDTO> itens = p.getItens().stream()
                .map(PedidoResponse::toItemDTO).toList();
        return new PedidoDTO(
                p.getId(), p.getStatus().name(), p.getValor(), p.getDesconto(),
                p.getImpostos(), p.getValorCobrado(), p.getEnderecoEntrega(), itens);
    }

    private static ItemDTO toItemDTO(ItemPedido item) {
        return new ItemDTO(
                item.getItem().getId(),
                item.getItem().getDescricao(),
                item.getItem().getPreco(),
                item.getQuantidade());
    }

    public PedidoDTO getPedido()                 { return pedido; }
    public boolean isAprovado()                  { return aprovado; }
    public String getMensagem()                  { return mensagem; }
    public List<ItemDTO> getItensIndisponiveis() { return itensIndisponiveis; }
}
