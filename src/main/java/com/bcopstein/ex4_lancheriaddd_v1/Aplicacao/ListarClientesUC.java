package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

@Component
public class ListarClientesUC {

    private final ClienteService clienteService;

    @Autowired
    public ListarClientesUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public List<Cliente> run() {
        return clienteService.listarClientes();
    }
}
