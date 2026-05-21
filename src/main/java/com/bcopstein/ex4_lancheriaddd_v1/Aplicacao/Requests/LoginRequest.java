package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

/**
 * Request para o caso de uso de login
 * Contém as credenciais fornecidas pelo usuário
 */
public class LoginRequest {
    private String email;
    private String senha;

    public LoginRequest() {
    }

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
