package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final IEstoqueService estoqueService;
    private final IImpostoService impostoService;
    private final IDescontoService descontoService;

    @Autowired
    public PedidoService(
            PedidoRepository pedidoRepository,
            IEstoqueService estoqueService,
            IImpostoService impostoService,
            IDescontoService descontoService) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueService   = estoqueService;
        this.impostoService   = impostoService;
        this.descontoService  = descontoService;
    }

    public Pedido processarSubmissao(Cliente cliente, List<ItemPedido> itens) {

        if (!estoqueService.verificarEstoque(itens)) {

            throw new RuntimeException(
                "Estoque insuficiente: um ou mais ingredientes não estão disponíveis");
        }

        double subtotal = itens.stream()
                .mapToDouble(item -> (double) item.getItem().getPreco() * item.getQuantidade())
                .sum();

        double desconto = descontoService.calcularDesconto(subtotal, cliente.getCpf());

        double impostos = impostoService.calcularImposto(subtotal);

        double valorCobrado = (subtotal - desconto) + impostos;

        Pedido pedidoAprovado = new Pedido(
                0,
                cliente,
                null,           
                itens,
                Pedido.Status.APROVADO,
                subtotal,
                impostos,
                desconto,
                valorCobrado
        );

        return pedidoRepository.salvar(pedidoAprovado);
    }
}
