package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class ListarPedidosEntreguesUC {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public ListarPedidosEntreguesUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> run(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.listarEntreguesEntreDatas(inicio, fim);
    }
}
