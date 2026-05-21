package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoCalculador.ValorPedidoDto;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido criarEAprovarPedido(Cliente cliente, String enderecoEntrega,
                                      List<ItemPedido> itens, ValorPedidoDto valores) {
        Pedido pedido = new Pedido(
                0, cliente, enderecoEntrega, null,
                itens, Pedido.Status.NOVO,
                0, 0, 0, 0
        );
        pedido.aprovar(valores.subtotal, valores.impostos, valores.desconto, valores.valorCobrado);
        return pedidoRepository.salvar(pedido);
    }
}
