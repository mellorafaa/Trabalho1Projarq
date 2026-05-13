package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoCalculador.ValorPedidoDto;

/**
 * Serviço de domínio responsável pela criação e persistência de pedidos.
 * Delega validações e cálculos para serviços especializados (PedidoValidador e PedidoCalculador).
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Cria um pedido aprovado com base no cliente, itens e valores calculados.
     * Responsabilidade única: criar e persistir a entidade Pedido.
     */
    public Pedido criarPedidoAprovado(Cliente cliente, List<ItemPedido> itens, ValorPedidoDto valores) {
        Pedido pedidoAprovado = new Pedido(
                0,
                cliente,
                null,
                itens,
                Pedido.Status.APROVADO,
                valores.subtotal,
                valores.impostos,
                valores.desconto,
                valores.valorCobrado
        );

        return pedidoRepository.salvar(pedidoAprovado);
    }
}
