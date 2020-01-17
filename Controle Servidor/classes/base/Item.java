/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Base;

import Util.Dado;

/**
 * @author Guilherme
 */
public class Item extends Dado{

    private String item = "";
    private String plano = "";
    private String tipo = "";
    private String custo = "";
    private long valor;
    private long saldo;
    private String descricao = "";
    private String unidade = "";
    private long qntTotal;
    private long precoUnitario;
    private long precoTotal;
    private String numeroMapa = "";
    private String descricaoMapa = "";
    private int idItem;

    /**
     *
     * @return
     */
    public int getIdItem(){
        return idItem;
    }

    /**
     *
     * @param idItem
     */
    public void setIdItem(int idItem){
        this.idItem = idItem;
    }
    
    /**
     *
     * @return
     */
    public String getItem(){
        return item;
    }

    /**
     *
     * @param item
     */
    public void setItem(String item){
        this.item = item;
    }

    /**
     *
     * @return
     */
    public String getPlano(){
        return plano;
    }

    /**
     *
     * @param plano
     */
    public void setPlano(String plano){
        this.plano = plano;
    }

    /**
     *
     * @return
     */
    public String getTipo(){
        return tipo;
    }

    /**
     *
     * @param tipo
     */
    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    /**
     *
     * @return
     */
    public String getCusto(){
        return custo;
    }

    /**
     *
     * @param custo
     */
    public void setCusto(String custo){
        this.custo = custo;
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
    public String getUnidade(){
        return unidade;
    }

    /**
     *
     * @param unidade
     */
    public void setUnidade(String unidade){
        this.unidade = unidade;
    }

    /**
     *
     * @return
     */
    public long getQntTotal(){
        return qntTotal;
    }

    /**
     *
     * @param qntTotal
     */
    public void setQntTotal(long qntTotal){
        this.qntTotal = qntTotal;
    }

    /**
     *
     * @return
     */
    public long getPrecoUnitario(){
        return precoUnitario;
    }

    /**
     *
     * @param precoUnitario
     */
    public void setPrecoUnitario(long precoUnitario){
        this.precoUnitario = precoUnitario;
    }

    /**
     *
     * @return
     */
    public long getPrecoTotal(){
        return precoTotal;
    }

    /**
     *
     * @param precoTotal
     */
    public void setPrecoTotal(long precoTotal){
        this.precoTotal = precoTotal;
    }

    /**
     *
     * @return
     */
    public String getNumeroMapa(){
        return numeroMapa;
    }

    /**
     *
     * @param numeroMapa
     */
    public void setNumeroMapa(String numeroMapa){
        this.numeroMapa = numeroMapa;
    }

    /**
     *
     * @return
     */
    public String getDescricaoMapa(){
        return descricaoMapa;
    }

    /**
     *
     * @param descricaoMapa
     */
    public void setDescricaoMapa(String descricaoMapa){
        this.descricaoMapa = descricaoMapa;
    }

}
