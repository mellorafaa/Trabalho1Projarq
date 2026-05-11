package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Component
public class PedidoRepositoryJDBC implements PedidoRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ClienteRepository  clienteRepository;
    private final ProdutosRepository produtosRepository;

    @Autowired
    public PedidoRepositoryJDBC(
            JdbcTemplate jdbcTemplate,
            ClienteRepository clienteRepository,
            ProdutosRepository produtosRepository) {
        this.jdbcTemplate      = jdbcTemplate;
        this.clienteRepository = clienteRepository;
        this.produtosRepository = produtosRepository;
    }

    @Override
    public Pedido salvar(Pedido pedido) {

        String sqlPedido =
            "INSERT INTO pedidos " +
            "(cliente_cpf, status, valor, impostos, desconto, valor_cobrado, data_hora_pagamento) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setString(2, pedido.getStatus().name());          
            ps.setDouble(3, pedido.getValor());
            ps.setDouble(4, pedido.getImpostos());
            ps.setDouble(5, pedido.getDesconto());
            ps.setDouble(6, pedido.getValorCobrado());
            ps.setObject(7, pedido.getDataHoraPagamento() != null
                    ? Timestamp.valueOf(pedido.getDataHoraPagamento())
                    : null);
            return ps;
        }, keyHolder);

        long idGerado = keyHolder.getKey().longValue();

        String sqlItem =
            "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";

        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(
                    sqlItem,
                    idGerado,
                    item.getItem().getId(),
                    item.getQuantidade()
            );
        }

        return recuperarPorId(idGerado);
    }

    @Override
    public Pedido recuperarPorId(long id) {
        String sqlPedido =
            "SELECT id, cliente_cpf, status, valor, impostos, desconto, " +
            "       valor_cobrado, data_hora_pagamento " +
            "FROM pedidos WHERE id = ?";

        List<Pedido> pedidos = jdbcTemplate.query(
                sqlPedido,
                ps -> ps.setLong(1, id),
                (rs, rowNum) -> {
                    Cliente cliente = clienteRepository.recuperarPorCpf(
                            rs.getString("cliente_cpf"));

                    Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));

                    LocalDateTime dataHoraPagamento = null;
                    Timestamp ts = rs.getTimestamp("data_hora_pagamento");
                    if (ts != null) {
                        dataHoraPagamento = ts.toLocalDateTime();
                    }

                    return new Pedido(
                            rs.getLong("id"),
                            cliente,
                            dataHoraPagamento,
                            new ArrayList<>(),   
                            status,
                            rs.getDouble("valor"),
                            rs.getDouble("impostos"),
                            rs.getDouble("desconto"),
                            rs.getDouble("valor_cobrado")
                    );
                }
        );

        if (pedidos.isEmpty()) {
            return null;
        }

        Pedido pedidoParcial = pedidos.get(0);

        List<ItemPedido> itens = recuperarItensDoPedido(id);

        return new Pedido(
                pedidoParcial.getId(),
                pedidoParcial.getCliente(),
                pedidoParcial.getDataHoraPagamento(),
                itens,
                pedidoParcial.getStatus(),
                pedidoParcial.getValor(),
                pedidoParcial.getImpostos(),
                pedidoParcial.getDesconto(),
                pedidoParcial.getValorCobrado()
        );
    }

    private List<ItemPedido> recuperarItensDoPedido(long pedidoId) {
        String sql =
            "SELECT produto_id, quantidade FROM itens_pedido WHERE pedido_id = ?";

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, pedidoId),
                (rs, rowNum) -> {
                    // Recupera o Produto completo (com Receita e Ingredientes)
                    Produto produto = produtosRepository.recuperaProdutoPorid(
                            rs.getLong("produto_id"));
                    return new ItemPedido(produto, rs.getInt("quantidade"));
                }
        );
    }

    @Override
    public long contarPedidosRecentesPorCliente(String clienteCpf, LocalDateTime desde) {
        
        String sql =
            "SELECT COUNT(*) FROM pedidos " +
            "WHERE cliente_cpf = ? AND data_criacao >= ?";

        Long count = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                clienteCpf,
                Timestamp.valueOf(desde)
        );

        return count != null ? count : 0L;
    }
}
