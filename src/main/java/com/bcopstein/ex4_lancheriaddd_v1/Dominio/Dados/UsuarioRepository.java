package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public interface UsuarioRepository {
    
    Usuario recuperarPorEmail(String email);
    Usuario salvar(Usuario usuario);
    Usuario recuperarPorId(String id);
}
