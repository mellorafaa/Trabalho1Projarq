package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ICozinhaService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPagamentoService;

@Component
public class PagarPedidoUC {

    private final PedidoRepository pedidoRepository;
    private final IPagamentoService pagamentoService;
    private final ICozinhaService cozinhaService;

    public PagarPedidoUC(PedidoRepository pedidoRepository,
                         IPagamentoService pagamentoService,
                         ICozinhaService cozinhaService) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoService = pagamentoService;
        this.cozinhaService   = cozinhaService;
    }

    public PagarPedidoResponse run(long idPedido) {

        Pedido pedido = pedidoRepository.recuperarPorId(idPedido);
        if (pedido == null) {
            return new PagarPedidoResponse(
                false,
                "Pedido não encontrado com o id: " + idPedido,
                idPedido,
                null
            );
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            return new PagarPedidoResponse(
                false,
                construirMotivoRejeicao(pedido.getStatus()),
                idPedido,
                pedido.getStatus().name()
            );
        }

        boolean autorizado = pagamentoService.processarPagamento(pedido);
        if (!autorizado) {
            return new PagarPedidoResponse(
                false,
                "Pagamento não autorizado para o pedido " + idPedido + ".",
                idPedido,
                pedido.getStatus().name()
            );
        }

        pedido.pagar();
        pedidoRepository.registrarPagamento(idPedido, LocalDateTime.now());

        cozinhaService.chegadaDePedido(pedido);

        return new PagarPedidoResponse(
            true,
            "Pagamento confirmado. Pedido " + idPedido + " encaminhado para a cozinha.",
            idPedido,
            Pedido.Status.PAGO.name()
        );
    }

    private String construirMotivoRejeicao(Pedido.Status statusAtual) {
        return switch (statusAtual) {
            case PAGO ->
                "Pedido já foi pago.";
            case CANCELADO ->
                "Pedido foi cancelado e não pode ser pago.";
            case NOVO ->
                "Pedido ainda não foi aprovado (status: NOVO).";
            default ->
                "Pedido não pode ser pago. Status atual: " + statusAtual + ".";
        };
    }
}
