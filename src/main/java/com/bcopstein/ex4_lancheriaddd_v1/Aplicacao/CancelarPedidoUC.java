package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

/**
 * UC6 — Cancelar Pedido
 *
 * Especificação (enunciado do trabalho):
 *   "Cliente solicita o cancelamento de um pedido aprovado, mas não pago."
 *
 * ─── Regras de negócio ────────────────────────────────────────────────────
 *
 *   1. O pedido deve existir na base de dados.
 *
 *   2. O pedido deve estar com status APROVADO.
 *      - Apenas pedidos APROVADOS podem ser cancelados.
 *      - Um pedido PAGO já teve a transação financeira confirmada;
 *        a partir desse momento "não pode mais ser cancelado" (enunciado).
 *      - Pedidos em outros estados (AGUARDANDO, PREPARACAO, etc.) também
 *        não podem ser cancelados pois já estão em produção.
 *
 *   3. Se as regras forem satisfeitas, o status é alterado para CANCELADO.
 *
 * ─── Fluxo principal (caminho feliz) ─────────────────────────────────────
 *
 *   [Entrada] idPedido
 *     → recuperarPorId(id)          → pedido encontrado
 *     → verificar status            → APROVADO ✓
 *     → atualizarStatus(CANCELADO)
 *     → retornar response { cancelado=true }
 *
 * ─── Fluxos alternativos ──────────────────────────────────────────────────
 *
 *   FA1 — Pedido não encontrado:
 *     → retornar response { cancelado=false, mensagem="Pedido não encontrado" }
 *
 *   FA2 — Status PAGO:
 *     → retornar response { cancelado=false, mensagem="Pedido já pago..." }
 *
 *   FA3 — Qualquer outro status não cancelável:
 *     → retornar response { cancelado=false, mensagem="Status atual: X" }
 *
 * ─── Posição na arquitetura ───────────────────────────────────────────────
 *
 *   Este use case pertence à camada de APLICAÇÃO e depende APENAS de
 *   interfaces do Domínio (PedidoRepository). Nunca depende de:
 *   - JdbcTemplate (infraestrutura)
 *   - HttpServletRequest (framework web)
 *   - Nenhuma classe de Adaptadores
 */
@Component
public class CancelarPedidoUC {

    // Dependência injetada via interface do Domínio (Princípio da Inversão de Dependência)
    // O Spring injeta automaticamente a implementação PedidoRepositoryJDBC
    private final PedidoRepository pedidoRepository;

    @Autowired
    public CancelarPedidoUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Executa o caso de uso de cancelamento de pedido.
     *
     * @param idPedido identificador do pedido que o cliente deseja cancelar
     * @return CancelarPedidoResponse com o resultado (sucesso ou motivo de falha)
     */
    public CancelarPedidoResponse run(long idPedido) {

        // ── Passo 1: buscar o pedido ──────────────────────────────────────────
        // Se o id não existir na base, encerra com falha imediatamente.
        // Não há sentido em tentar cancelar um pedido que não existe.
        Pedido pedido = pedidoRepository.recuperarPorId(idPedido);
        if (pedido == null) {
            return new CancelarPedidoResponse(
                false,
                "Pedido não encontrado com o id: " + idPedido,
                idPedido
            );
        }

        // ── Passo 2: verificar se o status permite cancelamento ───────────────
        // A regra do enunciado é clara: apenas pedidos APROVADOS podem ser
        // cancelados. Pedidos PAGOS ou em produção não podem ser revertidos.
        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            return new CancelarPedidoResponse(
                false,
                construirMotivoRejeicao(pedido.getStatus()),
                idPedido
            );
        }

        // ── Passo 3: efetivar o cancelamento ─────────────────────────────────
        // Status atualizado de APROVADO → CANCELADO no banco de dados.
        // O pedido permanece registrado para fins de histórico/auditoria.
        pedidoRepository.atualizarStatus(idPedido, Pedido.Status.CANCELADO);

        return new CancelarPedidoResponse(
            true,
            "Pedido " + idPedido + " cancelado com sucesso.",
            idPedido
        );
    }

    /**
     * Constrói uma mensagem de rejeição clara para cada status não permitido.
     *
     * Separado em método privado para manter o método run() legível e focado
     * no fluxo principal, sem poluição de condicionais de mensagem.
     */
    private String construirMotivoRejeicao(Pedido.Status statusAtual) {
        return switch (statusAtual) {
            case PAGO ->
                // Regra explícita do enunciado: "a partir deste momento não pode mais ser cancelado"
                "Pedido já foi pago e não pode ser cancelado.";
            case CANCELADO ->
                "Pedido já está cancelado.";
            case NOVO ->
                // NOVO antecede APROVADO; tecnicamente não deveria chegar aqui no fluxo normal
                "Pedido ainda não foi aprovado (status: NOVO).";
            default ->
                // AGUARDANDO, PREPARACAO, PRONTO, TRANSPORTE, ENTREGUE
                "Pedido não pode ser cancelado. Status atual: " + statusAtual + ".";
        };
    }
}
