/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Util;

/**
 * @author Guilherme
 */
public class ItemOrcamento{


    private int id = 0;
    private String item = "", planoConta = "", descricao = "", unidade = "";
    private long quantidadeTotal, precoUnitario, precoTotal;

    /**
     *
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
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
    public String getPlanoConta(){
        return planoConta;
    }

    /**
     *
     * @param planoConta
     */
    public void setPlanoConta(String planoConta){
        this.planoConta = planoConta;
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
    public long getQuantidadeTotal(){
        return quantidadeTotal;
    }

    /**
     *
     * @param quantidadeTotal
     */
    public void setQuantidadeTotal(long quantidadeTotal){
        this.quantidadeTotal = quantidadeTotal;
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
}
