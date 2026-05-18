package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new RuntimeException("Cliente inválido.");
        }

        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            throw new RuntimeException("CPF é obrigatório.");
        }
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório.");
        }
        if (cliente.getCelular() == null || cliente.getCelular().isBlank()) {
            throw new RuntimeException("Celular é obrigatório.");
        }
        if (cliente.getEndereco() == null || cliente.getEndereco().isBlank()) {
            throw new RuntimeException("Endereço é obrigatório.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new RuntimeException("Email é obrigatório.");
        }
        if (cliente.getSenha() == null || cliente.getSenha().isBlank()) {
            throw new RuntimeException("Senha é obrigatória.");
        }

        if (clienteRepository.recuperarPorCpf(cliente.getCpf()) != null) {
            throw new RuntimeException("Já existe um cliente cadastrado com o CPF informado.");
        }

        if (clienteRepository.recuperarPorEmail(cliente.getEmail()) != null) {
            throw new RuntimeException("Já existe um cliente cadastrado com o email informado.");
        }

        return clienteRepository.salvar(cliente);
    }
}
