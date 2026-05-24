package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import java.util.List;
import java.util.UUID;

@Repository
public class UsuarioRepositoryJDBC implements UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Usuario recuperarPorEmail(String email) {
        String sql = "SELECT id, email, senha_hash, nome, role, ativo FROM usuarios WHERE email = ?";
        List<Usuario> usuarios = jdbcTemplate.query(sql, usuarioRowMapper(), email);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    @Override
    public Usuario recuperarPorId(String id) {
        String sql = "SELECT id, email, senha_hash, nome, role, ativo FROM usuarios WHERE id = ?";
        List<Usuario> usuarios = jdbcTemplate.query(sql, usuarioRowMapper(), id);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, email, senha_hash, nome, role, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(sql,
                id,
                usuario.getEmail(),
                usuario.getSenhaHash(),
                usuario.getNome(),
                usuario.getRole(),
                usuario.isAtivo() ? 1 : 0
        );
        return recuperarPorId(id);
    }

    private RowMapper<Usuario> usuarioRowMapper() {
        return (rs, rowNum) -> new Usuario(
                rs.getString("id"),
                rs.getString("email"),
                rs.getString("senha_hash"),
                rs.getString("nome"),
                rs.getString("role"),
                rs.getInt("ativo") == 1
        );
    }
}
