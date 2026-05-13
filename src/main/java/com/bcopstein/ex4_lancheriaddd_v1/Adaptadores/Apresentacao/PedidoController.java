package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PedidoSubmissaoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

/**
 * Adaptador de entrada (input adapter) para operações de pedido.
 *
 * Responsabilidades do controller (Clean Architecture):
 *   1. Receber a requisição HTTP e extrair parâmetros
 *   2. Delegar toda a lógica de negócio ao Use Case correspondente
 *   3. Converter o resultado em resposta HTTP (status code + corpo JSON)
 *
 * O controller NÃO contém lógica de negócio — ele apenas orquestra.
 *
 * Endpoints:
 *   GET  /pedidos               → lista todos os pedidos
 *   POST /pedidos               → UC4 — Submeter pedido para aprovação
 *   POST /pedidos/{id}/cancelar → UC6 — Cancelar pedido aprovado
 *
 * Códigos HTTP:
 *   200 OK                    → operação bem-sucedida
 *   422 Unprocessable Entity  → regra de negócio impediu a operação
 *                               (pedido negado, pedido pago, não encontrado etc.)
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final ListarPedidosUC  listarPedidosUC;
    private final CancelarPedidoUC cancelarPedidoUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC,
                            ListarPedidosUC listarPedidosUC,
                            CancelarPedidoUC cancelarPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.listarPedidosUC  = listarPedidosUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
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

    /**
     * UC6 — Cancelar um pedido aprovado.
     *
     * O cliente informa o id do pedido que deseja cancelar.
     * O sistema verifica se o pedido está com status APROVADO e, se sim,
     * atualiza para CANCELADO.
     *
     * Regra central: pedidos PAGOS (ou em estados posteriores) não podem
     * ser cancelados — confirmação explícita do enunciado.
     *
     * Exemplos:
     *   POST /pedidos/1/cancelar  → 200 { cancelado:true,  mensagem:"Pedido 1 cancelado..." }
     *   POST /pedidos/2/cancelar  → 422 { cancelado:false, mensagem:"Pedido já foi pago..." }
     *   POST /pedidos/99/cancelar → 422 { cancelado:false, mensagem:"Pedido não encontrado..." }
     */
    @PostMapping("/{id}/cancelar")
    @CrossOrigin("*")
    public ResponseEntity<CancelarPedidoResponse> cancelarPedido(
            @PathVariable(value = "id") long id) {

        // Delega toda a lógica ao UC6 — o controller apenas converte o resultado em HTTP
        CancelarPedidoResponse response = cancelarPedidoUC.run(id);

        if (response.isCancelado()) {
            // 200 OK: cancelamento efetuado com sucesso
            return ResponseEntity.ok(response);
        }

        // 422 Unprocessable Entity: a requisição estava bem formada, mas o estado
        // atual do pedido impede a operação (regra de negócio violada)
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
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
