package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PedidoSubmissaoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final ListarPedidosUC listarPedidosUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC, ListarPedidosUC listarPedidosUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.listarPedidosUC  = listarPedidosUC;
    }

    @GetMapping
    @CrossOrigin("*")
    public ResponseEntity<List<PedidoPresenter>> listarPedidos() {
        List<Pedido> pedidos = listarPedidosUC.run();
        List<PedidoPresenter> presenters = pedidos.stream()
                .map(p -> montarPresenter(new PedidoResponse(p, true, "OK")))
                .toList();
        return ResponseEntity.ok(presenters);
    }

    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<PedidoPresenter> submeterPedido(
            @RequestBody PedidoSubmissaoRequest request) {

        PedidoResponse response = submeterPedidoUC.run(
                request.getClienteCpf(),
                request.getItens()
        );

        PedidoPresenter presenter = montarPresenter(response);

        if (response.isAprovado()) {
            return ResponseEntity.ok(presenter);
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(presenter);
        }
    }

    private PedidoPresenter montarPresenter(PedidoResponse response) {

        if (!response.isAprovado() || response.getPedido() == null) {
            return new PedidoPresenter(
                    0, "NEGADO", 0, 0, 0, 0,
                    false, response.getMensagem(), List.of()
            );
        }

        List<PedidoPresenter.ItemPedidoPresenter> itensPresenter =
                response.getPedido().getItens().stream()
                        .map(item -> new PedidoPresenter.ItemPedidoPresenter(
                                item.getItem().getId(),
                                item.getItem().getDescricao(),
                                item.getItem().getPreco(),
                                item.getQuantidade()
                        ))
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
                itensPresenter
        );
    }
}
