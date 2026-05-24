package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.SolicitacaoItem;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoCalculador;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoCalculador.ValorPedidoDto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoValidador;

@Component
public class SubmeterPedidoUC {

    private final PedidoValidador pedidoValidador;
    private final PedidoCalculador pedidoCalculador;
    private final PedidoService pedidoService;

    public SubmeterPedidoUC(
            PedidoValidador pedidoValidador,
            PedidoCalculador pedidoCalculador,
            PedidoService pedidoService) {
        this.pedidoValidador  = pedidoValidador;
        this.pedidoCalculador = pedidoCalculador;
        this.pedidoService    = pedidoService;
    }

    public PedidoResponse run(String clienteCpf, String enderecoEntrega,
                              List<ItemPedidoRequest> itensSolicitados) {

        if (!validarDadosEntrada(itensSolicitados)) {
            return new PedidoResponse(null, false,
                    "O pedido deve conter pelo menos um item", List.of());
        }

        if (enderecoEntrega == null || enderecoEntrega.isBlank()) {
            return new PedidoResponse(null, false,
                    "Endereço de entrega é obrigatório", List.of());
        }

        String erroValidacao = validarItens(itensSolicitados);
        if (erroValidacao != null) {
            return new PedidoResponse(null, false, erroValidacao, List.of());
        }

        try {
            List<SolicitacaoItem> solicitacoes = converterParaSolicitacoes(itensSolicitados);

            Cliente cliente = pedidoValidador.validarCliente(clienteCpf);
            List<ItemPedido> itens = pedidoValidador.validarEConverterItens(solicitacoes);

            List<ItemPedido> itensIndisponiveis = pedidoValidador.verificarEstoque(itens);
            if (!itensIndisponiveis.isEmpty()) {
                return new PedidoResponse(null, false,
                        "Pedido negado por falta de ingredientes", itensIndisponiveis);
            }

            ValorPedidoDto valores = pedidoCalculador.calcularValorCompleto(itens, clienteCpf);

            Pedido pedidoAprovado = pedidoService.criarEAprovarPedido(
                    cliente, enderecoEntrega, itens, valores);

            return new PedidoResponse(pedidoAprovado, true,
                    "Pedido aprovado com sucesso! Número: " + pedidoAprovado.getId(), List.of());

        } catch (RuntimeException e) {
            return new PedidoResponse(null, false, e.getMessage(), List.of());
        }
    }

    private boolean validarDadosEntrada(List<ItemPedidoRequest> itensSolicitados) {
        return itensSolicitados != null && !itensSolicitados.isEmpty();
    }

    private String validarItens(List<ItemPedidoRequest> itensSolicitados) {
        for (ItemPedidoRequest itemRequest : itensSolicitados) {
            if (itemRequest.getQuantidade() <= 0) {
                return "Quantidade inválida para o produto ID " + itemRequest.getProdutoId()
                        + ": deve ser maior que zero";
            }
        }
        return null;
    }

    private List<SolicitacaoItem> converterParaSolicitacoes(List<ItemPedidoRequest> itensSolicitados) {
        List<SolicitacaoItem> solicitacoes = new ArrayList<>();
        for (ItemPedidoRequest itemRequest : itensSolicitados) {
            solicitacoes.add(new SolicitacaoItem(itemRequest.getProdutoId(), itemRequest.getQuantidade()));
        }
        return solicitacoes;
    }
}
