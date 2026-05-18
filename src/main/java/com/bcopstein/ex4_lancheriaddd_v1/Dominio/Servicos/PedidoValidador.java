package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.SolicitacaoItem;

@Service
public class PedidoValidador {

    private final ClienteRepository clienteRepository;
    private final ProdutosRepository produtosRepository;
    private final IEstoqueService estoqueService;

    @Autowired
    public PedidoValidador(
            ClienteRepository clienteRepository,
            ProdutosRepository produtosRepository,
            IEstoqueService estoqueService) {
        this.clienteRepository = clienteRepository;
        this.produtosRepository = produtosRepository;
        this.estoqueService = estoqueService;
    }

    public Cliente validarCliente(String clienteCpf) {
        Cliente cliente = clienteRepository.recuperarPorCpf(clienteCpf);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado com CPF: " + clienteCpf);
        }
        return cliente;
    }

    public List<ItemPedido> validarEConverterItens(List<SolicitacaoItem> itensSolicitados) {
        List<ItemPedido> itens = new java.util.ArrayList<>();
        
        for (SolicitacaoItem solicitacao : itensSolicitados) {
            Produto produto = produtosRepository.recuperaProdutoPorid(solicitacao.getProdutoId());
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado com ID: " + solicitacao.getProdutoId());
            }
            itens.add(new ItemPedido(produto, solicitacao.getQuantidade()));
        }
        
        return itens;
    }

    public List<ItemPedido> verificarEstoque(List<ItemPedido> itens) {
        return estoqueService.verificarEstoque(itens);
    }
}
