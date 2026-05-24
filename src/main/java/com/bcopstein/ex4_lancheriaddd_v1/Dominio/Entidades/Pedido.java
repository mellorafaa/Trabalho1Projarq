package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    public enum Status {
        NOVO, APROVADO, PAGO, AGUARDANDO,
        PREPARACAO, PRONTO, TRANSPORTE, ENTREGUE, CANCELADO
    }

    private final long id;
    private final Cliente cliente;
    private final String enderecoEntrega;
    private final LocalDateTime dataHoraPagamento;
    private final List<ItemPedido> itens;
    private Status status;
    private double valor;
    private double impostos;
    private double desconto;
    private double valorCobrado;

    public Pedido(long id, Cliente cliente, String enderecoEntrega, LocalDateTime dataHoraPagamento,
                  List<ItemPedido> itens, Status status, double valor, double impostos,
                  double desconto, double valorCobrado) {
        this.id = id;
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
        this.dataHoraPagamento = dataHoraPagamento;
        this.itens = itens;
        this.status = status;
        this.valor = valor;
        this.impostos = impostos;
        this.desconto = desconto;
        this.valorCobrado = valorCobrado;
    }

    public void aprovar(double valor, double impostos, double desconto, double valorCobrado) {
        this.status = Status.APROVADO;
        this.valor = valor;
        this.impostos = impostos;
        this.desconto = desconto;
        this.valorCobrado = valorCobrado;
    }

    public void pagar()            { this.status = Status.PAGO; }
    public void cancelar()         { this.status = Status.CANCELADO; }
    public void iniciarPreparo()   { this.status = Status.AGUARDANDO; }
    public void comecarPreparacao(){ this.status = Status.PREPARACAO; }
    public void marcarPronto()     { this.status = Status.PRONTO; }
    public void iniciarTransporte(){ this.status = Status.TRANSPORTE; }
    public void marcarEntregue()   { this.status = Status.ENTREGUE; }

    public long getId()                         { return id; }
    public Cliente getCliente()                 { return cliente; }
    public String getEnderecoEntrega()          { return enderecoEntrega; }
    public LocalDateTime getDataHoraPagamento() { return dataHoraPagamento; }
    public List<ItemPedido> getItens()          { return itens; }
    public Status getStatus()                   { return status; }
    public double getValor()                    { return valor; }
    public double getImpostos()                 { return impostos; }
    public double getDesconto()                 { return desconto; }
    public double getValorCobrado()             { return valorCobrado; }
}
