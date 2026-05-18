package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ClienteRepository {

    Cliente recuperarPorCpf(String cpf);

    Cliente recuperarPorEmail(String email);

    Cliente salvar(Cliente cliente);
}
