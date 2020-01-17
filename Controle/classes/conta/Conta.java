/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.conta;

/**
 *
 * @author guilherme.machado
 */
public class Conta {

    private int id, nivel;
    private String nome = "", login = "", categoria = "", senha = "", email = "";
    private boolean block, compra, almoxarife, administracao, engenharia, financeiro, gerencia, logado;
    private long ultimoLogin, expira;

    public Conta() {
        this(-1, 0, "", "", "", "", "", true, false, false, false, false, false, false, true, 0, 0);
    }

    public Conta(int id, int nivel, String nome, String login, String categoria, String senha, String email, boolean block, boolean compra, boolean almoxarife, boolean administracao, boolean engenharia, boolean financeiro, boolean gerencia, boolean logado, long ultimoLogin, long expira) {
        this.id = id;
        this.nivel = nivel;
        this.nome = nome;
        this.login = login;
        this.categoria = categoria;
        this.senha = senha;
        this.email = email;
        this.block = block;
        this.compra = compra;
        this.almoxarife = almoxarife;
        this.administracao = administracao;
        this.engenharia = engenharia;
        this.financeiro = financeiro;
        this.gerencia = gerencia;
        this.logado = logado;
        this.ultimoLogin = ultimoLogin;
        this.expira = expira;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean isCompra() {
        return compra;
    }

    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    public boolean isAlmoxarife() {
        return almoxarife;
    }

    public void setAlmoxarife(boolean almoxarife) {
        this.almoxarife = almoxarife;
    }

    public boolean isAdministracao() {
        return administracao;
    }

    public void setAdministracao(boolean administracao) {
        this.administracao = administracao;
    }

    public boolean isEngenharia() {
        return engenharia;
    }

    public void setEngenharia(boolean engenharia) {
        this.engenharia = engenharia;
    }

    public boolean isFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(boolean financeiro) {
        this.financeiro = financeiro;
    }

    public boolean isGerencia() {
        return gerencia;
    }

    public void setGerencia(boolean gerencia) {
        this.gerencia = gerencia;
    }

    public boolean isLogado() {
        return logado;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    public long getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(long ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public long getExpira() {
        return expira;
    }

    public void setExpira(long expira) {
        this.expira = expira;
    }

}
