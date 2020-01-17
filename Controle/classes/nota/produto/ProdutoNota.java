/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.nota.produto;

import classes.base.Produto;

/**
 * @author Guilherme
 */
public class ProdutoNota extends Produto {

    private String ncm, cst, cfop, lote;
    private long qnt;
    private long precoUnitario;
    private long precoTotal;
    private int planoConta;

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public long getQnt() {
        return qnt;
    }

    public void setQnt(long qnt) {
        this.qnt = qnt;
    }

    public long getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(long precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public long getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(long precoTotal) {
        this.precoTotal = precoTotal;
    }

    public int getPlanoConta() {
        return planoConta;
    }

    public void setPlanoConta(int planoConta) {
        this.planoConta = planoConta;
    }

    
    
}
