package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class CancelarPedidoUC {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public CancelarPedidoUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public CancelarPedidoResponse run(long idPedido) {

        Pedido pedido = pedidoRepository.recuperarPorId(idPedido);
        if (pedido == null) {
            return new CancelarPedidoResponse(
                false,
                "Pedido não encontrado com o id: " + idPedido,
                idPedido
            );
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            return new CancelarPedidoResponse(
                false,
                construirMotivoRejeicao(pedido.getStatus()),
                idPedido
            );
        }

        pedidoRepository.atualizarStatus(idPedido, Pedido.Status.CANCELADO);

        return new CancelarPedidoResponse(
            true,
            "Pedido " + idPedido + " cancelado com sucesso.",
            idPedido
        );
    }

    private String construirMotivoRejeicao(Pedido.Status statusAtual) {
        return switch (statusAtual) {
            case PAGO ->
                "Pedido já foi pago e não pode ser cancelado.";
            case CANCELADO ->
                "Pedido já está cancelado.";
            case NOVO ->
                "Pedido ainda não foi aprovado (status: NOVO).";
            default ->
                "Pedido não pode ser cancelado. Status atual: " + statusAtual + ".";
        };
    }
}
