package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class SolicitarStatusPedidoUC {

  private final PedidoRepository pedidoRepository;

  @Autowired
  public SolicitarStatusPedidoUC(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  public Pedido run(long pedidoId) {
    return pedidoRepository.recuperarPorId(pedidoId);
  }
}
