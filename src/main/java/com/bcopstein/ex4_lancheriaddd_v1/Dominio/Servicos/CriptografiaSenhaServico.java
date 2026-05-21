package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

public interface CriptografiaSenhaServico {
    
    String criptografar(String senhaTextoPlano);
    
    boolean verificar(String senhaTextoPlano, String senhaHash);
}
