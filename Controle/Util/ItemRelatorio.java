/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author guilherme.machado
 */
public class ItemRelatorio {

    private String numeroMapa;
    private String fornecedor;
    private String descricao;
    private String itemOrcamento;
    private String planoContas;
    private String notaFiscal;
    private String retencao;
    private String total;
    private String emissao;

    public ItemRelatorio(String numeroMapa, String fornecedor, String descricao, String itemOrcamento, String planoContas, String notaFiscal, String retencao, String total, String emissao) {
        this.numeroMapa = numeroMapa;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.itemOrcamento = itemOrcamento;
        this.planoContas = planoContas;
        this.notaFiscal = notaFiscal;
        this.retencao = retencao;
        this.total = total;
        this.emissao = emissao;
    }
    
    public String getNumeroMapa() {
        return numeroMapa;
    }

    public void setNumeroMapa(String numeroMapa) {
        this.numeroMapa = numeroMapa;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getItemOrcamento() {
        return itemOrcamento;
    }

    public void setItemOrcamento(String itemOrcamento) {
        this.itemOrcamento = itemOrcamento;
    }

    public String getPlanoContas() {
        return planoContas;
    }

    public void setPlanoContas(String planoContas) {
        this.planoContas = planoContas;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getRetencao() {
        return retencao;
    }

    public void setRetencao(String retencao) {
        this.retencao = retencao;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    
}
