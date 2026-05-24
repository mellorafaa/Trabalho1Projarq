package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

@Component
public class ListarClientesUC {

    private final ClienteService clienteService;

    public ListarClientesUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public List<ClienteResponse> run() {
        return clienteService.listarClientes().stream()
                .map(c -> new ClienteResponse(
                        c.getCpf(), c.getNome(), c.getCelular(), c.getEndereco(), c.getEmail()))
                .toList();
    }
}
