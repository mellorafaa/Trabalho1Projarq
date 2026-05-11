package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoUC {

    private final PedidoService pedidoService;
    private final ClienteRepository clienteRepository;
    private final ProdutosRepository produtosRepository;

    @Autowired
    public SubmeterPedidoUC(
            PedidoService pedidoService,
            ClienteRepository clienteRepository,
            ProdutosRepository produtosRepository) {
        this.pedidoService       = pedidoService;
        this.clienteRepository   = clienteRepository;
        this.produtosRepository  = produtosRepository;
    }

    public PedidoResponse run(String clienteCpf, List<ItemPedidoRequest> itensSolicitados) {

        Cliente cliente = clienteRepository.recuperarPorCpf(clienteCpf);
        if (cliente == null) {
            return new PedidoResponse(null, false,
                    "Cliente não encontrado com CPF: " + clienteCpf);
        }

        if (itensSolicitados == null || itensSolicitados.isEmpty()) {
            return new PedidoResponse(null, false,
                    "O pedido deve conter pelo menos um item");
        }

        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoRequest itemRequest : itensSolicitados) {

            // Valida a quantidade solicitada
            if (itemRequest.getQuantidade() <= 0) {
                return new PedidoResponse(null, false,
                        "Quantidade inválida para o produto ID " + itemRequest.getProdutoId()
                                + ": deve ser maior que zero");
            }

            Produto produto = produtosRepository.recuperaProdutoPorid(itemRequest.getProdutoId());
            if (produto == null) {
                return new PedidoResponse(null, false,
                        "Produto não encontrado com ID: " + itemRequest.getProdutoId());
            }

            itens.add(new ItemPedido(produto, itemRequest.getQuantidade()));
        }

        try {
            Pedido pedidoAprovado = pedidoService.processarSubmissao(cliente, itens);
            return new PedidoResponse(pedidoAprovado, true,
                    "Pedido aprovado com sucesso! Número: " + pedidoAprovado.getId());

        } catch (RuntimeException e) {
            return new PedidoResponse(null, false, e.getMessage());
        }
    }
}
