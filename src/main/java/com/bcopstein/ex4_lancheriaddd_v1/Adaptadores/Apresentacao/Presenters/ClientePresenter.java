package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

public class ClientePresenter {
    private final String cpf;
    private final String nome;
    private final String celular;
    private final String endereco;
    private final String email;

    public ClientePresenter(String cpf, String nome, String celular, String endereco, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.celular = celular;
        this.endereco = endereco;
        this.email = email;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getCelular() { return celular; }
    public String getEndereco() { return endereco; }
    public String getEmail() { return email; }
}
