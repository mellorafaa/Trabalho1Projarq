package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class SolicitarStatusPedidoUC {

    private final PedidoRepository pedidoRepository;

    public SolicitarStatusPedidoUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public PedidoStatusResponse run(long pedidoId) {
        Pedido pedido = pedidoRepository.recuperarPorId(pedidoId);
        if (pedido == null) return null;
        return new PedidoStatusResponse(pedido.getId(), pedido.getStatus().name());
    }
}
