package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Usuario {

    private String id;
    private String email;
    private String senhaHash;
    private String nome;
    private String role;
    private boolean ativo;

    public Usuario(String id, String email, String senhaHash, String nome, String role, boolean ativo) {
        this.id = id;
        this.email = email;
        this.senhaHash = senhaHash;
        this.nome = nome;
        this.role = role;
        this.ativo = ativo;
    }

    // Construtor para criação de novo usuário
    public Usuario(String email, String senhaHash, String nome, String role) {
        this.id = null;
        this.email = email;
        this.senhaHash = senhaHash;
        this.nome = nome;
        this.role = role;
        this.ativo = true;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getNome() {
        return nome;
    }

    public String getRole() {
        return role;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // Validações de domínio
    public boolean senhaCorresponde(String senhaCandidato, String senhaHashArmazenada) {
        return senhaCandidato != null && senhaHashArmazenada != null
                && senhaCandidato.equals(senhaHashArmazenada);
    }

    public boolean estaAtivo() {
        return this.ativo;
    }
}
