package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Cliente {

    private final String cpf;
    private final String nome;
    private final String celular;
    private final String endereco;
    private final String email;
    private final String senhaHash;

    public Cliente(String cpf, String nome, String celular, String endereco, String email) {
        this(cpf, nome, celular, endereco, email, null);
    }

    public Cliente(String cpf, String nome, String celular, String endereco, String email, String senhaHash) {
        this.cpf = cpf;
        this.nome = nome;
        this.celular = celular;
        this.endereco = endereco;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public String getCpf()       { return cpf; }
    public String getNome()      { return nome; }
    public String getCelular()   { return celular; }
    public String getEndereco()  { return endereco; }
    public String getEmail()     { return email; }
    public String getSenhaHash() { return senhaHash; }
}
