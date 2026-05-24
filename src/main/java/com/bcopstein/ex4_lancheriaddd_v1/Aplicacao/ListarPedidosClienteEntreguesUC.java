package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;

@Component
public class ListarPedidosClienteEntreguesUC {

    private final PedidoRepository pedidoRepository;

    public ListarPedidosClienteEntreguesUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoResponse> run(String cpf, LocalDate inicio, LocalDate fim) {
        return pedidoRepository.listarEntreguesEntreDatasParaCliente(cpf, inicio, fim).stream()
                .map(p -> new PedidoResponse(p, true, "OK", List.of()))
                .toList();
    }
}
