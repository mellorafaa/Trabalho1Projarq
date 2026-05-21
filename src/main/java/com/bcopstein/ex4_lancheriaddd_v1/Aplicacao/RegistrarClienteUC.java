package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CriptografiaSenhaServico;

@Component
public class RegistrarClienteUC {

    private final ClienteService clienteService;
    private final UsuarioRepository usuarioRepository;
    private final CriptografiaSenhaServico criptografiaSenhaServico;

    @Autowired
    public RegistrarClienteUC(ClienteService clienteService,
                               UsuarioRepository usuarioRepository,
                               CriptografiaSenhaServico criptografiaSenhaServico) {
        this.clienteService = clienteService;
        this.usuarioRepository = usuarioRepository;
        this.criptografiaSenhaServico = criptografiaSenhaServico;
    }

    public RegistrarClienteResponse run(RegistrarClienteRequest request) {
        if (request == null) {
            return new RegistrarClienteResponse(false, "Dados de cadastro não informados.");
        }

        try {
            Cliente cliente = new Cliente(
                request.getCpf(),
                request.getNome(),
                request.getCelular(),
                request.getEndereco(),
                request.getEmail(),
                request.getSenha()
            );

            clienteService.cadastrarCliente(cliente);

            String senhaHash = criptografiaSenhaServico.criptografar(request.getSenha());
            Usuario usuario = new Usuario(request.getEmail(), senhaHash, request.getNome(), "USER");
            usuarioRepository.salvar(usuario);

            return new RegistrarClienteResponse(true, "Cliente cadastrado com sucesso.");
        } catch (RuntimeException e) {
            return new RegistrarClienteResponse(false, e.getMessage());
        }
    }
}
