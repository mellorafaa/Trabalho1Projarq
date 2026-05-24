package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;

@Service
public class DescontoService implements IDescontoService {

    private static final double TAXA_DESCONTO = 0.07;

    private static final int MINIMO_PEDIDOS_PARA_DESCONTO = 3;

    private static final int DIAS_HISTORICO = 20;

    private final PedidoRepository pedidoRepository;

    public DescontoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public double calcularDesconto(double subtotal, String clienteCpf) {
        LocalDateTime vinteDiasAtras = LocalDateTime.now().minusDays(DIAS_HISTORICO);

        long pedidosPagos = pedidoRepository.contarPedidosPagosPorCliente(clienteCpf, vinteDiasAtras);

        if (pedidosPagos >= MINIMO_PEDIDOS_PARA_DESCONTO) {
            return subtotal * TAXA_DESCONTO;
        }

        return 0.0;
    }
}
