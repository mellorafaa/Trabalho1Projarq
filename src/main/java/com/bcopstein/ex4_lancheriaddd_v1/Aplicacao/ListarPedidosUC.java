package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;

@Component
public class ListarPedidosUC {

    private final PedidoRepository pedidoRepository;

    public ListarPedidosUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoResponse> run() {
        return pedidoRepository.listarTodos().stream()
                .map(p -> new PedidoResponse(p, true, "OK", List.of()))
                .toList();
    }
}
