package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public interface IGeradorToken {
    String gerarToken(Usuario usuario);
    long getTempoExpiracaoMs();
}
