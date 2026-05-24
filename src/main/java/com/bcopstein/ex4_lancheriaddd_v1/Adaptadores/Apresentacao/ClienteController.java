package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarClientesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RegistrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.ClientePresenter;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final RegistrarClienteUC registrarClienteUC;
    private final ListarClientesUC listarClientesUC;

    public ClienteController(RegistrarClienteUC registrarClienteUC, ListarClientesUC listarClientesUC) {
        this.registrarClienteUC = registrarClienteUC;
        this.listarClientesUC = listarClientesUC;
    }

    @GetMapping
    @CrossOrigin("*")
    public List<ClientePresenter> listarClientes() {
        return listarClientesUC.run().stream()
                .map(r -> new ClientePresenter(r.cpf(), r.nome(), r.celular(), r.endereco(), r.email()))
                .toList();
    }

    @PostMapping
    @CrossOrigin("*")
    public RegistrarClienteResponse registrarCliente(@RequestBody RegistrarClienteRequest request) {
        return registrarClienteUC.run(request);
    }
}
