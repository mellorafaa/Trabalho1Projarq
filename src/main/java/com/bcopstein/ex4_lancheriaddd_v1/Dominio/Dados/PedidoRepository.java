package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);

    Pedido recuperarPorId(long id);

    long contarPedidosRecentesPorCliente(String clienteCpf, LocalDateTime desde);
}
