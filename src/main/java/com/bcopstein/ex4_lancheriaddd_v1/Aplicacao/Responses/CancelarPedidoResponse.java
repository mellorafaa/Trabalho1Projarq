package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

/**
 * DTO de saída do UC6 — Cancelar Pedido.
 *
 * Segue o mesmo padrão de todas as Response classes do projeto:
 * representa o resultado de um caso de uso sem expor entidades de domínio
 * diretamente ao controller.
 *
 * Campos:
 *   - cancelado : true se o cancelamento foi efetuado com sucesso
 *   - mensagem  : descrição do resultado (sucesso ou motivo da recusa)
 *   - idPedido  : id do pedido que foi (ou tentou ser) cancelado
 */
public class CancelarPedidoResponse {

    private final boolean cancelado;
    private final String mensagem;
    private final long idPedido;

    public CancelarPedidoResponse(boolean cancelado, String mensagem, long idPedido) {
        this.cancelado = cancelado;
        this.mensagem  = mensagem;
        this.idPedido  = idPedido;
    }

    public boolean isCancelado() { return cancelado; }
    public String getMensagem()  { return mensagem; }
    public long getIdPedido()    { return idPedido; }
}
