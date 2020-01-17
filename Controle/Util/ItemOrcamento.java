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

    public ItemOrcamento(int id,String item, String planoConta, String descricao, String unidade, long quantidadeTotal, long precoUnitario, long precoTotal){
        this.id = id;
        this.item = item;
        this.planoConta = planoConta;
        this.descricao = descricao;
        this.unidade = unidade;
        this.quantidadeTotal = quantidadeTotal;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
    }

    public ItemOrcamento(){

    }
    private int id = 0;
    private String item = "", planoConta = "", descricao = "", unidade = "";
    private long quantidadeTotal, precoUnitario, precoTotal;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }  
    
    public String getItem(){
        return item;
    }

    public void setItem(String item){
        this.item = item;
    }

    public String getPlanoConta(){
        return planoConta;
    }

    public void setPlanoConta(String planoConta){
        this.planoConta = planoConta;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getUnidade(){
        return unidade;
    }

    public void setUnidade(String unidade){
        this.unidade = unidade;
    }

    public long getQuantidadeTotal(){
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(long quantidadeTotal){
        this.quantidadeTotal = quantidadeTotal;
    }

    public long getPrecoUnitario(){
        return precoUnitario;
    }

    public void setPrecoUnitario(long precoUnitario){
        this.precoUnitario = precoUnitario;
    }

    public long getPrecoTotal(){
        return precoTotal;
    }

    public void setPrecoTotal(long precoTotal){
        this.precoTotal = precoTotal;
    }

}
