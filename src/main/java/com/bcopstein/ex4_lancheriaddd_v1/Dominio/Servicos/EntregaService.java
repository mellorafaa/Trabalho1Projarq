package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class EntregaService implements IEntregaService {

    private static final Logger log = LoggerFactory.getLogger(EntregaService.class);

    private final PedidoRepository pedidoRepository;

    private final Queue<Pedido> filaTransporte;
    private Pedido emTransporte;
    private final ScheduledExecutorService scheduler;

    public EntregaService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.filaTransporte   = new LinkedBlockingQueue<>();
        this.emTransporte     = null;
        this.scheduler        = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public synchronized void chegadaDePedido(Pedido p) {
        filaTransporte.add(p);
        log.info("Pedido na fila de entrega: {}", p.getId());
        if (emTransporte == null) {
            atribuirEntregador(filaTransporte.poll());
        }
    }

    private synchronized void atribuirEntregador(Pedido pedido) {
        pedido.iniciarTransporte();
        pedidoRepository.atualizarStatus(pedido.getId(), Pedido.Status.TRANSPORTE);
        emTransporte = pedido;
        log.info("Pedido em transporte: {}", pedido.getId());
        scheduler.schedule(() -> pedidoEntregue(), 5, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void pedidoEntregue() {
        if (emTransporte == null) {
            return;
        }
        emTransporte.marcarEntregue();
        pedidoRepository.atualizarStatus(emTransporte.getId(), Pedido.Status.ENTREGUE);
        log.info("Pedido entregue: {}", emTransporte.getId());
        emTransporte = null;

        if (!filaTransporte.isEmpty()) {
            Pedido prox = filaTransporte.poll();
            scheduler.schedule(() -> atribuirEntregador(prox), 1, TimeUnit.SECONDS);
        }
    }
}
