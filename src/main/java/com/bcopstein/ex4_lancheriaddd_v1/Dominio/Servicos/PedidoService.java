package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.SolicitacaoItem;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutosRepository produtosRepository;
    private final IEstoqueService estoqueService;
    private final IImpostoService impostoService;
    private final IDescontoService descontoService;

    @Autowired
    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutosRepository produtosRepository,
            IEstoqueService estoqueService,
            IImpostoService impostoService,
            IDescontoService descontoService) {
        this.pedidoRepository  = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtosRepository = produtosRepository;
        this.estoqueService    = estoqueService;
        this.impostoService    = impostoService;
        this.descontoService   = descontoService;
    }

    public Pedido processarSubmissao(String clienteCpf, List<SolicitacaoItem> itensSolicitados) {

        Cliente cliente = clienteRepository.recuperarPorCpf(clienteCpf);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado com CPF: " + clienteCpf);
        }

        List<ItemPedido> itens = new ArrayList<>();
        for (SolicitacaoItem solicitacao : itensSolicitados) {
            Produto produto = produtosRepository.recuperaProdutoPorid(solicitacao.getProdutoId());
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado com ID: " + solicitacao.getProdutoId());
            }
            itens.add(new ItemPedido(produto, solicitacao.getQuantidade()));
        }

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
