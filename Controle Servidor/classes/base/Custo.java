/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Base;

import Util.Dado;
import java.util.Date;

/**
 *
 * @author guilherme.machado
 */
public class Custo extends Dado {

    private String numero = "";
    private String fornecedor = "";
    private String descricao = "";
    private long valorTotal;
    private long gastoTotal;
    private long saldoTotal;
    private String observacao = "";
    private String contrato = "";
    private Date emissao = new Date();
    private int idFornecedor;

    /**
     *
     * @return
     */
    public String getNumero() {
        return numero;
    }

    /**
     *
     * @param numero
     */
    public void setNumero(String numero) {
        if(numero == null)return;
        this.numero = numero;
    }

    /**
     *
     * @return
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     *
     * @param fornecedor
     */
    public void setFornecedor(String fornecedor) {
        if(fornecedor == null)return;
        this.fornecedor = fornecedor;
    }

    /**
     *
     * @return
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     *
     * @param descricao
     */
    public void setDescricao(String descricao) {
        if(descricao == null)return;
        this.descricao = descricao;
    }

    /**
     *
     * @return
     */
    public long getValorTotal() {
        return valorTotal;
    }

    /**
     *
     * @param valorTotal
     */
    public void setValorTotal(long valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     *
     * @return
     */
    public long getGastoTotal() {
        return gastoTotal;
    }

    /**
     *
     * @param gastoTotal
     */
    public void setGastoTotal(long gastoTotal) {
        this.gastoTotal = gastoTotal;
    }

    /**
     *
     * @return
     */
    public long getSaldoTotal() {
        return saldoTotal;
    }

    /**
     *
     * @param saldoTotal
     */
    public void setSaldoTotal(long saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    /**
     *
     * @return
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     *
     * @param observacao
     */
    public void setObservacao(String observacao) {
        if(observacao == null)return;
        this.observacao = observacao;
    }

    /**
     *
     * @return
     */
    public Date getEmissao() {
        return emissao;
    }

    /**
     *
     * @param emissao
     */
    public void setEmissao(Date emissao) {
        if(emissao == null)return;
        this.emissao = emissao;
    }

    /**
     *
     * @return
     */
    public String getContrato() {
        return contrato;
    }

    /**
     *
     * @param contrato
     */
    public void setContrato(String contrato) {
        if(contrato == null)return;
        this.contrato = contrato;
    }

    /**
     *
     * @return
     */
    public int getIdFornecedor(){
        return idFornecedor;
    }

    /**
     *
     * @param idFornecedor
     */
    public void setIdFornecedor(int idFornecedor){
        this.idFornecedor = idFornecedor;
    }

    
    
}
