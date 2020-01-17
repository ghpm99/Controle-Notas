/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Base;

import Util.Dado;

/**
 *
 * @author guilherme.machado
 */
public class Mapa extends Dado{

    private String numero;
    private String descricao;
    private String empresa;
    private long valor;
    private long total;
    private long saldo;
    private String contrato;

    /**
     *
     * @return
     */
    public String getNumero(){
        return numero;
    }

    /**
     *
     * @param numero
     */
    public void setNumero(String numero){
        this.numero = numero;
    }

    /**
     *
     * @return
     */
    public String getDescricao(){
        return descricao;
    }

    /**
     *
     * @param descricao
     */
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    /**
     *
     * @return
     */
    public String getEmpresa(){
        return empresa;
    }

    /**
     *
     * @param empresa
     */
    public void setEmpresa(String empresa){
        this.empresa = empresa;
    }

    /**
     *
     * @return
     */
    public long getValor(){
        return valor;
    }

    /**
     *
     * @param valor
     */
    public void setValor(long valor){
        this.valor = valor;
    }

    /**
     *
     * @return
     */
    public long getTotal(){
        return total;
    }

    /**
     *
     * @param total
     */
    public void setTotal(long total){
        this.total = total;
    }

    /**
     *
     * @return
     */
    public long getSaldo(){
        return saldo;
    }

    /**
     *
     * @param saldo
     */
    public void setSaldo(long saldo){
        this.saldo = saldo;
    }

    /**
     *
     * @return
     */
    public String getContrato(){
        return contrato;
    }

    /**
     *
     * @param contrato
     */
    public void setContrato(String contrato){
        this.contrato = contrato;
    }

}
