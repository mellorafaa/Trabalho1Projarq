package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Usuario {

    private final String id;
    private final String email;
    private final String senhaHash;
    private final String nome;
    private final String role;
    private final boolean ativo;

    public Usuario(String id, String email, String senhaHash, String nome, String role, boolean ativo) {
        this.id = id;
        this.email = email;
        this.senhaHash = senhaHash;
        this.nome = nome;
        this.role = role;
        this.ativo = ativo;
    }

    public Usuario(String email, String senhaHash, String nome, String role) {
        this(null, email, senhaHash, nome, role, true);
    }

    public String getId()        { return id; }
    public String getEmail()     { return email; }
    public String getSenhaHash() { return senhaHash; }
    public String getNome()      { return nome; }
    public String getRole()      { return role; }
    public boolean isAtivo()     { return ativo; }
    public boolean estaAtivo()   { return ativo; }
}
