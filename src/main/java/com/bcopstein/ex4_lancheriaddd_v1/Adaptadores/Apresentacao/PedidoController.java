package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoStatusPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PedidoSubmissaoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SolicitarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final ListarPedidosUC listarPedidosUC;
    private final SolicitarStatusPedidoUC solicitarStatusPedidoUC;
    private final CancelarPedidoUC cancelarPedidoUC;
    private final PagarPedidoUC pagarPedidoUC;
    private final ListarPedidosEntreguesUC listarPedidosEntreguesUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC,
                            ListarPedidosUC listarPedidosUC,
                            SolicitarStatusPedidoUC solicitarStatusPedidoUC,
                            CancelarPedidoUC cancelarPedidoUC,
                            PagarPedidoUC pagarPedidoUC,
                            ListarPedidosEntreguesUC listarPedidosEntreguesUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.listarPedidosUC = listarPedidosUC;
        this.solicitarStatusPedidoUC = solicitarStatusPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
    }

    @GetMapping("/entregues")
    @CrossOrigin("*")
    public ResponseEntity<List<PedidoPresenter>> listarPedidosEntregues(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Pedido> pedidos = listarPedidosEntreguesUC.run(inicio, fim);
        List<PedidoPresenter> presenters = pedidos.stream()
                .map(p -> montarPresenter(new PedidoResponse(p, true, "OK", List.of())))
                .toList();
        return ResponseEntity.ok(presenters);
    }

    @GetMapping
    @CrossOrigin("*")
    public ResponseEntity<List<PedidoPresenter>> listarPedidos() {
        List<Pedido> pedidos = listarPedidosUC.run();
        List<PedidoPresenter> presenters = pedidos.stream()
                .map(p -> montarPresenter(new PedidoResponse(p, true, "OK", List.of())))
                .toList();
        return ResponseEntity.ok(presenters);
    }

    @GetMapping("/{id}")
    @CrossOrigin("*")
    public ResponseEntity<PedidoStatusPresenter> recuperarStatusPedido(@PathVariable long id) {
        Pedido pedido = solicitarStatusPedidoUC.run(id);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(new PedidoStatusPresenter(pedido.getId(), pedido.getStatus().name()));
    }

    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<PedidoPresenter> submeterPedido(@RequestBody PedidoSubmissaoRequest request) {
        PedidoResponse response = submeterPedidoUC.run(
                request.getClienteCpf(),
                request.getEnderecoEntrega(),
                request.getItens()
        );

        PedidoPresenter presenter = montarPresenter(response);

        if (response.isAprovado()) {
            return ResponseEntity.ok(presenter);
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(presenter);
        }
    }

    @PostMapping("/{id}/cancelar")
    @CrossOrigin("*")
    public ResponseEntity<CancelarPedidoResponse> cancelarPedido(@PathVariable long id) {
        CancelarPedidoResponse response = cancelarPedidoUC.run(id);

        if (response.isCancelado()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @PostMapping("/{id}/pagar")
    @CrossOrigin("*")
    public ResponseEntity<PagarPedidoResponse> pagarPedido(@PathVariable long id) {
        PagarPedidoResponse response = pagarPedidoUC.run(id);

        if (response.isPago()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    private PedidoPresenter montarPresenter(PedidoResponse response) {
        List<PedidoPresenter.ItemPedidoPresenter> indisponiveis =
                response.getItensIndisponiveis().stream()
                        .map(item -> new PedidoPresenter.ItemPedidoPresenter(
                                item.getItem().getId(),
                                item.getItem().getDescricao(),
                                item.getItem().getPreco(),
                                item.getQuantidade()
                        ))
                        .collect(Collectors.toList());

        if (!response.isAprovado() || response.getPedido() == null) {
            return new PedidoPresenter(
                    0, "NEGADO", 0, 0, 0, 0,
                    false, response.getMensagem(), "",
                    List.of(), indisponiveis
            );
        }

        List<PedidoPresenter.ItemPedidoPresenter> itensPresenter = response.getPedido().getItens().stream()
                .map(item -> new PedidoPresenter.ItemPedidoPresenter(
                        item.getItem().getId(),
                        item.getItem().getDescricao(),
                        item.getItem().getPreco(),
                        item.getQuantidade()))
                .collect(Collectors.toList());

        return new PedidoPresenter(
                response.getPedido().getId(),
                response.getPedido().getStatus().name(),
                response.getPedido().getValor(),
                response.getPedido().getDesconto(),
                response.getPedido().getImpostos(),
                response.getPedido().getValorCobrado(),
                response.isAprovado(),
                response.getMensagem(),
                response.getPedido().getEnderecoEntrega(),
                itensPresenter,
                List.of()
        );
    }
}
