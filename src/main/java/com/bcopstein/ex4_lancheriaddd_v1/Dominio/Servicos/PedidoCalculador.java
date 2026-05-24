package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

/**
 * Serviço de domínio responsável pelos cálculos de valores do pedido.
 * Encapsula toda a lógica de cálculo de subtotal, impostos e descontos.
 */
@Service
public class PedidoCalculador {

    private final IImpostoService impostoService;
    private final IDescontoService descontoService;

    public PedidoCalculador(IImpostoService impostoService, IDescontoService descontoService) {
        this.impostoService = impostoService;
        this.descontoService = descontoService;
    }

    /**
     * Calcula o subtotal (quantidade * preço) de todos os itens
     */
    public double calcularSubtotal(List<ItemPedido> itens) {
        return itens.stream()
                .mapToDouble(item -> (double) item.getItem().getPreco() * item.getQuantidade())
                .sum();
    }

    /**
     * Calcula o desconto aplicável para o cliente
     */
    public double calcularDesconto(double subtotal, String clienteCpf) {
        return descontoService.calcularDesconto(subtotal, clienteCpf);
    }

    /**
     * Calcula os impostos sobre o subtotal
     */
    public double calcularImpostos(double subtotal) {
        return impostoService.calcularImposto(subtotal);
    }

    /**
     * Calcula o valor final a ser cobrado: (subtotal - desconto) + impostos
     */
    public double calcularValorCobrado(double subtotal, double desconto, double impostos) {
        return (subtotal - desconto) + impostos;
    }

    /**
     * Realiza todos os cálculos de uma vez
     */
    public ValorPedidoDto calcularValorCompleto(List<ItemPedido> itens, String clienteCpf) {
        double subtotal = calcularSubtotal(itens);
        double desconto = calcularDesconto(subtotal, clienteCpf);
        double impostos = calcularImpostos(subtotal);
        double valorCobrado = calcularValorCobrado(subtotal, desconto, impostos);

        return new ValorPedidoDto(subtotal, desconto, impostos, valorCobrado);
    }

    /**
     * DTO para encapsular os valores calculados do pedido
     */
    public static class ValorPedidoDto {
        public final double subtotal;
        public final double desconto;
        public final double impostos;
        public final double valorCobrado;

        public ValorPedidoDto(double subtotal, double desconto, double impostos, double valorCobrado) {
            this.subtotal = subtotal;
            this.desconto = desconto;
            this.impostos = impostos;
            this.valorCobrado = valorCobrado;
        }
    }
}
