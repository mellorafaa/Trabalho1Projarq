package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.util.List;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ClienteRepository {

    Cliente recuperarPorCpf(String cpf);

    Cliente recuperarPorEmail(String email);

    List<Cliente> recuperarTodos();

    Cliente salvar(Cliente cliente);
}
