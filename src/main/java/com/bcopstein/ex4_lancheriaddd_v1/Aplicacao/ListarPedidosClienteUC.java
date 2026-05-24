package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;

@Component
public class ListarPedidosClienteUC {

    private final PedidoRepository pedidoRepository;

    public ListarPedidosClienteUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoResponse> run(String clienteCpf) {
        if (clienteCpf == null || clienteCpf.isBlank()) {
            throw new IllegalArgumentException("CPF do cliente não pode estar vazio");
        }
        return pedidoRepository.listarPorClienteCpf(clienteCpf).stream()
                .map(p -> new PedidoResponse(p, true, "OK", List.of()))
                .toList();
    }
}
