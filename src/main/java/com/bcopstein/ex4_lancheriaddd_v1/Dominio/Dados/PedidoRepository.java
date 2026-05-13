package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;
import java.util.List;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {

    Pedido salvar(Pedido pedido);

    Pedido recuperarPorId(long id);

    List<Pedido> listarTodos();

    long contarPedidosRecentesPorCliente(String clienteCpf, LocalDateTime desde);

    void atualizarStatus(long id, Pedido.Status novoStatus);
}
