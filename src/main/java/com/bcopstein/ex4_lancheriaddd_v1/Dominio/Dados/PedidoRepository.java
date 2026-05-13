package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;
import java.util.List;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

/**
 * Porta de saída (output port) do Domínio para persistência de pedidos.
 *
 * Na Clean Architecture, interfaces de repositório ficam no Domínio e são
 * implementadas nos Adaptadores (PedidoRepositoryJDBC). O Domínio nunca
 * conhece JdbcTemplate, SQL, nem nenhuma tecnologia de infraestrutura.
 */
public interface PedidoRepository {

    /**
     * Persiste um pedido novo e seus itens.
     * O ID é gerado automaticamente (AUTO_INCREMENT) e preenchido no retorno.
     */
    Pedido salvar(Pedido pedido);

    /**
     * Recupera um pedido completo (com cliente e itens) pelo id.
     * Retorna null se não encontrado.
     */
    Pedido recuperarPorId(long id);

    /**
     * Retorna todos os pedidos cadastrados (com seus itens).
     */
    List<Pedido> listarTodos();

    /**
     * Conta pedidos do cliente desde a data informada.
     * Usado pelo DescontoService para verificar o critério de fidelidade:
     * "mais de 3 pedidos nos últimos 20 dias → 7% de desconto".
     */
    long contarPedidosRecentesPorCliente(String clienteCpf, LocalDateTime desde);

    /**
     * Atualiza o status de um pedido.
     * Usado por:
     *   - UC6: APROVADO → CANCELADO
     *   - UC7 (futuro): APROVADO → PAGO
     */
    void atualizarStatus(long id, Pedido.Status novoStatus);
}
