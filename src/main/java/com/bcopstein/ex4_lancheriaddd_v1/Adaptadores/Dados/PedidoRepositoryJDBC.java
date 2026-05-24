package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final ClienteRepository clienteRepository;
    private final ProdutosRepository produtosRepository;

    public PedidoRepositoryJDBC(
            JdbcTemplate jdbcTemplate,
            ClienteRepository clienteRepository,
            ProdutosRepository produtosRepository) {
        this.jdbcTemplate       = jdbcTemplate;
        this.clienteRepository  = clienteRepository;
        this.produtosRepository = produtosRepository;
    }

    @Override
    public Pedido salvar(Pedido pedido) {

        String sqlPedido =
            "INSERT INTO pedidos " +
            "(cliente_cpf, status, valor, impostos, desconto, valor_cobrado, " +
            " data_hora_pagamento, endereco_entrega) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            ps.setString(8, pedido.getEnderecoEntrega() != null
                    ? pedido.getEnderecoEntrega()
                    : "");
            return ps;
        }, keyHolder);

        long idGerado = ((Number) keyHolder.getKeys().get("ID")).longValue();

        String sqlItem =
            "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";

        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(sqlItem, idGerado, item.getItem().getId(), item.getQuantidade());
        }

        return recuperarPorId(idGerado);
    }

    @Override
    public Pedido recuperarPorId(long id) {
        String sqlPedido =
            "SELECT id, cliente_cpf, status, valor, impostos, desconto, " +
            "       valor_cobrado, data_hora_pagamento, endereco_entrega " +
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
                    if (ts != null) dataHoraPagamento = ts.toLocalDateTime();

                    return new Pedido(
                            rs.getLong("id"),
                            cliente,
                            rs.getString("endereco_entrega"),
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

        if (pedidos.isEmpty()) return null;

        Pedido pedidoParcial = pedidos.get(0);
        List<ItemPedido> itens = recuperarItensDoPedido(id);

        return new Pedido(
                pedidoParcial.getId(),
                pedidoParcial.getCliente(),
                pedidoParcial.getEnderecoEntrega(),
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
        String sql = "SELECT produto_id, quantidade FROM itens_pedido WHERE pedido_id = ?";

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, pedidoId),
                (rs, rowNum) -> {
                    Produto produto = produtosRepository.recuperaProdutoPorId(
                            rs.getLong("produto_id"));
                    return new ItemPedido(produto, rs.getInt("quantidade"));
                }
        );
    }

    @Override
    public List<Pedido> listarTodos() {
        String sql =
            "SELECT id, cliente_cpf, status, valor, impostos, desconto, " +
            "       valor_cobrado, data_hora_pagamento, endereco_entrega FROM pedidos";

        List<Pedido> pedidosParciais = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Cliente cliente = clienteRepository.recuperarPorCpf(rs.getString("cliente_cpf"));
            Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));
            LocalDateTime dataHoraPagamento = null;
            Timestamp ts = rs.getTimestamp("data_hora_pagamento");
            if (ts != null) dataHoraPagamento = ts.toLocalDateTime();

            return new Pedido(
                    rs.getLong("id"),
                    cliente,
                    rs.getString("endereco_entrega"),
                    dataHoraPagamento,
                    new ArrayList<>(),
                    status,
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado")
            );
        });

        List<Pedido> pedidosCompletos = new ArrayList<>();
        for (Pedido p : pedidosParciais) {
            List<ItemPedido> itens = recuperarItensDoPedido(p.getId());
            pedidosCompletos.add(new Pedido(
                    p.getId(), p.getCliente(), p.getEnderecoEntrega(),
                    p.getDataHoraPagamento(), itens, p.getStatus(),
                    p.getValor(), p.getImpostos(), p.getDesconto(), p.getValorCobrado()
            ));
        }
        return pedidosCompletos;
    }

    @Override
    public List<Pedido> listarEntreguesEntreDatas(LocalDate inicio, LocalDate fim) {
        String sql =
            "SELECT id, cliente_cpf, status, valor, impostos, desconto, " +
            "       valor_cobrado, data_hora_pagamento, endereco_entrega " +
            "FROM pedidos " +
            "WHERE status = 'ENTREGUE' " +
            "  AND CAST(data_criacao AS DATE) BETWEEN ? AND ?";

        List<Pedido> pedidosParciais = jdbcTemplate.query(sql, ps -> {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
        }, (rs, rowNum) -> {
            Cliente cliente = clienteRepository.recuperarPorCpf(rs.getString("cliente_cpf"));
            Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));
            LocalDateTime dataHoraPagamento = null;
            Timestamp ts = rs.getTimestamp("data_hora_pagamento");
            if (ts != null) dataHoraPagamento = ts.toLocalDateTime();
            return new Pedido(
                    rs.getLong("id"),
                    cliente,
                    rs.getString("endereco_entrega"),
                    dataHoraPagamento,
                    new ArrayList<>(),
                    status,
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado")
            );
        });

        List<Pedido> pedidosCompletos = new ArrayList<>();
        for (Pedido p : pedidosParciais) {
            List<ItemPedido> itens = recuperarItensDoPedido(p.getId());
            pedidosCompletos.add(new Pedido(
                    p.getId(), p.getCliente(), p.getEnderecoEntrega(),
                    p.getDataHoraPagamento(), itens, p.getStatus(),
                    p.getValor(), p.getImpostos(), p.getDesconto(), p.getValorCobrado()
            ));
        }
        return pedidosCompletos;
    }

    @Override
    public List<Pedido> listarEntreguesEntreDatasParaCliente(String cpf, LocalDate inicio, LocalDate fim) {
        String sql =
            "SELECT id, cliente_cpf, status, valor, impostos, desconto, " +
            "       valor_cobrado, data_hora_pagamento, endereco_entrega " +
            "FROM pedidos " +
            "WHERE status = 'ENTREGUE' " +
            "  AND cliente_cpf = ? " +
            "  AND CAST(data_criacao AS DATE) BETWEEN ? AND ?";

        List<Pedido> pedidosParciais = jdbcTemplate.query(sql, ps -> {
            ps.setString(1, cpf);
            ps.setDate(2, Date.valueOf(inicio));
            ps.setDate(3, Date.valueOf(fim));
        }, (rs, rowNum) -> {
            Cliente cliente = clienteRepository.recuperarPorCpf(rs.getString("cliente_cpf"));
            Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));
            LocalDateTime dataHoraPagamento = null;
            Timestamp ts = rs.getTimestamp("data_hora_pagamento");
            if (ts != null) dataHoraPagamento = ts.toLocalDateTime();
            return new Pedido(
                    rs.getLong("id"),
                    cliente,
                    rs.getString("endereco_entrega"),
                    dataHoraPagamento,
                    new ArrayList<>(),
                    status,
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado")
            );
        });

        List<Pedido> pedidosCompletos = new ArrayList<>();
        for (Pedido p : pedidosParciais) {
            List<ItemPedido> itens = recuperarItensDoPedido(p.getId());
            pedidosCompletos.add(new Pedido(
                    p.getId(), p.getCliente(), p.getEnderecoEntrega(),
                    p.getDataHoraPagamento(), itens, p.getStatus(),
                    p.getValor(), p.getImpostos(), p.getDesconto(), p.getValorCobrado()
            ));
        }
        return pedidosCompletos;
    }

    @Override
    public long contarPedidosRecentesPorCliente(String clienteCpf, LocalDateTime desde) {
        String sql =
            "SELECT COUNT(*) FROM pedidos " +
            "WHERE cliente_cpf = ? AND data_criacao >= ?";

        Long count = jdbcTemplate.queryForObject(
                sql, Long.class, clienteCpf, Timestamp.valueOf(desde));

        return count != null ? count : 0L;
    }

    @Override
    public long contarPedidosPagosPorCliente(String clienteCpf, LocalDateTime desde) {
        String sql =
            "SELECT COUNT(*) FROM pedidos " +
            "WHERE cliente_cpf = ? AND data_criacao >= ? " +
            "  AND (status = 'PAGO' OR status = 'ENTREGUE')";

        Long count = jdbcTemplate.queryForObject(
                sql, Long.class, clienteCpf, Timestamp.valueOf(desde));

        return count != null ? count : 0L;
    }

    @Override
    public List<Pedido> listarPorClienteCpf(String cpf) {
        String sql =
            "SELECT id, cliente_cpf, status, valor, impostos, desconto, " +
            "       valor_cobrado, data_hora_pagamento, endereco_entrega " +
            "FROM pedidos " +
            "WHERE cliente_cpf = ?";

        List<Pedido> pedidosParciais = jdbcTemplate.query(sql, ps -> {
            ps.setString(1, cpf);
        }, (rs, rowNum) -> {
            Cliente cliente = clienteRepository.recuperarPorCpf(rs.getString("cliente_cpf"));
            Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));
            LocalDateTime dataHoraPagamento = null;
            Timestamp ts = rs.getTimestamp("data_hora_pagamento");
            if (ts != null) dataHoraPagamento = ts.toLocalDateTime();
            return new Pedido(
                    rs.getLong("id"),
                    cliente,
                    rs.getString("endereco_entrega"),
                    dataHoraPagamento,
                    new ArrayList<>(),
                    status,
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado")
            );
        });

        List<Pedido> pedidosCompletos = new ArrayList<>();
        for (Pedido p : pedidosParciais) {
            List<ItemPedido> itens = recuperarItensDoPedido(p.getId());
            pedidosCompletos.add(new Pedido(
                    p.getId(), p.getCliente(), p.getEnderecoEntrega(),
                    p.getDataHoraPagamento(), itens, p.getStatus(),
                    p.getValor(), p.getImpostos(), p.getDesconto(), p.getValorCobrado()
            ));
        }
        return pedidosCompletos;
    }

    @Override
    public void atualizarStatus(long id, Pedido.Status novoStatus) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, novoStatus.name(), id);
    }

    @Override
    public void registrarPagamento(long id, LocalDateTime dataHoraPagamento) {
        String sql = "UPDATE pedidos SET status = ?, data_hora_pagamento = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                Pedido.Status.PAGO.name(),
                Timestamp.valueOf(dataHoraPagamento),
                id
        );
    }
}
