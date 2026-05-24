package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

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
            String senhaHash = criptografiaSenhaServico.criptografar(request.getSenha());

            Cliente cliente = new Cliente(
                request.getCpf(),
                request.getNome(),
                request.getCelular(),
                request.getEndereco(),
                request.getEmail(),
                senhaHash
            );

            clienteService.cadastrarCliente(cliente);

            Usuario usuario = new Usuario(request.getEmail(), senhaHash, request.getNome(), "USER");
            usuarioRepository.salvar(usuario);

            return new RegistrarClienteResponse(true, "Cliente cadastrado com sucesso.");
        } catch (RuntimeException e) {
            return new RegistrarClienteResponse(false, e.getMessage());
        }
    }
}
