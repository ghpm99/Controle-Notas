/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Seguranca.Login;

/**
 *
 * @author guilherme.machado
 */
public class Conta {

    private int id, nivel;
    private String nome, login, categoria, senha, email;
    private boolean block, compra, almoxarife, administracao, engenharia, financeiro, gerencia, logado;
    private long ultimoLogin, expira;

    /**
     *
     */
    public Conta() {
        this(-1, 0, "", "", "", "", "", true, false, false, false, false, false, false, true, 0, 0);
    }

    /**
     *
     * @param id
     * @param nivel
     * @param nome
     * @param login
     * @param categoria
     * @param senha
     * @param email
     * @param block
     * @param compra
     * @param almoxarife
     * @param administracao
     * @param engenharia
     * @param financeiro
     * @param gerencia
     * @param logado
     * @param ultimoLogin
     * @param expira
     */
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

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getNivel() {
        return nivel;
    }

    /**
     *
     * @param nivel
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    /**
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     *
     * @param categoria
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     *
     * @return
     */
    public String getSenha() {
        return senha;
    }

    /**
     *
     * @param senha
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public boolean isBlock() {
        return block;
    }

    /**
     *
     * @param block
     */
    public void setBlock(boolean block) {
        this.block = block;
    }

    /**
     *
     * @return
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     *
     * @param compra
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    /**
     *
     * @return
     */
    public boolean isAlmoxarife() {
        return almoxarife;
    }

    /**
     *
     * @param almoxarife
     */
    public void setAlmoxarife(boolean almoxarife) {
        this.almoxarife = almoxarife;
    }

    /**
     *
     * @return
     */
    public boolean isAdministracao() {
        return administracao;
    }

    /**
     *
     * @param administracao
     */
    public void setAdministracao(boolean administracao) {
        this.administracao = administracao;
    }

    /**
     *
     * @return
     */
    public boolean isEngenharia() {
        return engenharia;
    }

    /**
     *
     * @param engenharia
     */
    public void setEngenharia(boolean engenharia) {
        this.engenharia = engenharia;
    }

    /**
     *
     * @return
     */
    public boolean isFinanceiro() {
        return financeiro;
    }

    /**
     *
     * @param financeiro
     */
    public void setFinanceiro(boolean financeiro) {
        this.financeiro = financeiro;
    }

    /**
     *
     * @return
     */
    public boolean isGerencia() {
        return gerencia;
    }

    /**
     *
     * @param gerencia
     */
    public void setGerencia(boolean gerencia) {
        this.gerencia = gerencia;
    }

    /**
     *
     * @return
     */
    public boolean isLogado() {
        return logado;
    }

    /**
     *
     * @param logado
     */
    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    /**
     *
     * @return
     */
    public long getUltimoLogin() {
        return ultimoLogin;
    }

    /**
     *
     * @param ultimoLogin
     */
    public void setUltimoLogin(long ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    /**
     *
     * @return
     */
    public long getExpira() {
        return expira;
    }

    /**
     *
     * @param expira
     */
    public void setExpira(long expira) {
        this.expira = expira;
    }

}
