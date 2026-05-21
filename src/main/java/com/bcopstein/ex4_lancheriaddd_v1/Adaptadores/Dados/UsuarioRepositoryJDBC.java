package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class UsuarioRepositoryJDBC implements UsuarioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public UsuarioRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Usuario recuperarPorEmail(String email) {
        String sql = "SELECT id, email, senha_hash, nome, role, ativo FROM usuarios WHERE email = ?";
        
        try {
            List<Usuario> usuarios = jdbcTemplate.query(sql, new Object[]{email}, usuarioRowMapper());
            return usuarios.isEmpty() ? null : usuarios.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Usuario recuperarPorId(String id) {
        String sql = "SELECT id, email, senha_hash, nome, role, ativo FROM usuarios WHERE id = ?";
        
        try {
            List<Usuario> usuarios = jdbcTemplate.query(sql, new Object[]{id}, usuarioRowMapper());
            return usuarios.isEmpty() ? null : usuarios.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Usuario salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, email, senha_hash, nome, role, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            String id = java.util.UUID.randomUUID().toString();
            jdbcTemplate.update(sql, 
                id,
                usuario.getEmail(),
                usuario.getSenhaHash(),
                usuario.getNome(),
                usuario.getRole(),
                usuario.isAtivo() ? 1 : 0
            );
            
            // Recuperar o usuário salvo
            return recuperarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mapper que converte ResultSet em Usuario
     */
    private RowMapper<Usuario> usuarioRowMapper() {
        return new RowMapper<Usuario>() {
            @Override
            public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Usuario(
                    rs.getString("id"),
                    rs.getString("email"),
                    rs.getString("senha_hash"),
                    rs.getString("nome"),
                    rs.getString("role"),
                    rs.getInt("ativo") == 1
                );
            }
        };
    }
}
