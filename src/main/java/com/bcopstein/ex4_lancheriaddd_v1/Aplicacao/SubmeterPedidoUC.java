package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Use Case de aplicação para submeter um pedido.
 * Responsável pela orquestração do fluxo:
 * 1. Validar dados de entrada
 * 2. Validar cliente e itens
 * 3. Calcular valores
 * 4. Criar e persistir o pedido
 */
@Component
public class SubmeterPedidoUC {

    private final PedidoValidador pedidoValidador;
    private final PedidoCalculador pedidoCalculador;
    private final PedidoService pedidoService;

    @Autowired
    public SubmeterPedidoUC(
            PedidoValidador pedidoValidador,
            PedidoCalculador pedidoCalculador,
            PedidoService pedidoService) {
        this.pedidoValidador = pedidoValidador;
        this.pedidoCalculador = pedidoCalculador;
        this.pedidoService = pedidoService;
    }

    public PedidoResponse run(String clienteCpf, List<ItemPedidoRequest> itensSolicitados) {

        // 1. Validar dados de entrada da aplicação
        if (!validarDadosEntrada(itensSolicitados)) {
            return new PedidoResponse(null, false, "O pedido deve conter pelo menos um item");
        }

        // 2. Validar dados individuais dos itens
        String erroValidacao = validarItens(itensSolicitados);
        if (erroValidacao != null) {
            return new PedidoResponse(null, false, erroValidacao);
        }

        try {
            // 3. Converter requests em solicitações de domínio
            List<SolicitacaoItem> solicitacoes = converterParaSolicitacoes(itensSolicitados);

            // 4. Validar cliente e existência dos produtos
            Cliente cliente = pedidoValidador.validarCliente(clienteCpf);
            List<ItemPedido> itens = pedidoValidador.validarEConverterItens(solicitacoes);

            // 5. Validar estoque
            pedidoValidador.validarEstoque(itens);

            // 6. Calcular valores do pedido
            ValorPedidoDto valores = pedidoCalculador.calcularValorCompleto(itens, clienteCpf);

            // 7. Criar e persistir o pedido
            Pedido pedidoAprovado = pedidoService.criarPedidoAprovado(cliente, itens, valores);

            return new PedidoResponse(pedidoAprovado, true,
                    "Pedido aprovado com sucesso! Número: " + pedidoAprovado.getId());

        } catch (RuntimeException e) {
            return new PedidoResponse(null, false, e.getMessage());
        }
    }

    /**
     * Valida se a lista de itens não é nula ou vazia
     */
    private boolean validarDadosEntrada(List<ItemPedidoRequest> itensSolicitados) {
        return itensSolicitados != null && !itensSolicitados.isEmpty();
    }

    /**
     * Valida quantidade de cada item
     */
    private String validarItens(List<ItemPedidoRequest> itensSolicitados) {
        for (ItemPedidoRequest itemRequest : itensSolicitados) {
            if (itemRequest.getQuantidade() <= 0) {
                return "Quantidade inválida para o produto ID " + itemRequest.getProdutoId()
                        + ": deve ser maior que zero";
            }
        }
        return null;
    }

    /**
     * Converte requests da aplicação em solicitações de domínio
     */
    private List<SolicitacaoItem> converterParaSolicitacoes(List<ItemPedidoRequest> itensSolicitados) {
        List<SolicitacaoItem> solicitacoes = new ArrayList<>();
        for (ItemPedidoRequest itemRequest : itensSolicitados) {
            solicitacoes.add(new SolicitacaoItem(itemRequest.getProdutoId(), itemRequest.getQuantidade()));
        }
        return solicitacoes;
    }
}
