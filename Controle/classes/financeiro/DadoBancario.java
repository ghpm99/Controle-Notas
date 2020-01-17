/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.financeiro;

import Util.Dado;

/**
 *
 * @author guilherme.machado
 */
public class DadoBancario extends Dado {
    
    private String cnpj = "", nome = "", agencia = "", conta = "";
    
    private Banco banco;
    
    private TipoConta tipo;
    
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }
    
    
}
