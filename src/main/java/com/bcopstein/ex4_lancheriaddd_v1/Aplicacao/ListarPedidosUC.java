package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class ListarPedidosUC {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public ListarPedidosUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> run() {
        return pedidoRepository.listarTodos();
    }
}
