package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.SolicitacaoItem;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoUC {

    private final PedidoService pedidoService;

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public PedidoResponse run(String clienteCpf, List<ItemPedidoRequest> itensSolicitados) {

        if (itensSolicitados == null || itensSolicitados.isEmpty()) {
            return new PedidoResponse(null, false, "O pedido deve conter pelo menos um item");
        }

        List<SolicitacaoItem> solicitacoes = new ArrayList<>();
        for (ItemPedidoRequest itemRequest : itensSolicitados) {
            if (itemRequest.getQuantidade() <= 0) {
                return new PedidoResponse(null, false,
                        "Quantidade inválida para o produto ID " + itemRequest.getProdutoId()
                                + ": deve ser maior que zero");
            }
            solicitacoes.add(new SolicitacaoItem(itemRequest.getProdutoId(), itemRequest.getQuantidade()));
        }

        try {
            Pedido pedidoAprovado = pedidoService.processarSubmissao(clienteCpf, solicitacoes);
            return new PedidoResponse(pedidoAprovado, true,
                    "Pedido aprovado com sucesso! Número: " + pedidoAprovado.getId());
        } catch (RuntimeException e) {
            return new PedidoResponse(null, false, e.getMessage());
        }
    }
}
