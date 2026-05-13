package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

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
