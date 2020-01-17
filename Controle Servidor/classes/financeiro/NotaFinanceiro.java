/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Financeiro;

import Classes.Nota.cobranca.Pagamento;
import java.util.Date;

/**
 * @author Guilherme
 */
public class NotaFinanceiro extends Pagamento{

    private int idDiario;
    private boolean baixada,classificada;
    private long desconto, juros, valorLiquido;
    private String  numero = "", fornecedor = "",cnpj = "";
    private Date fluxo = new Date(),vencimentoNota = new Date();

    /**
     *
     * @return
     */
    public int getIdDiario() {
        return idDiario;
    }

    /**
     *
     * @param idDiario
     */
    public void setIdDiario(int idDiario) {
        this.idDiario = idDiario;
    }

    /**
     *
     * @return
     */
    public boolean isBaixada() {
        return baixada;
    }

    /**
     *
     * @param baixada
     */
    public void setBaixada(boolean baixada) {
        this.baixada = baixada;
    }

    /**
     *
     * @return
     */
    public long getDesconto() {
        return desconto;
    }

    /**
     *
     * @param desconto
     */
    public void setDesconto(long desconto) {
        this.desconto = desconto;
    }

    /**
     *
     * @return
     */
    public long getJuros() {
        return juros;
    }

    /**
     *
     * @param juros
     */
    public void setJuros(long juros) {
        this.juros = juros;
    }

    /**
     *
     * @return
     */
    public long getValorLiquido() {
        return valorLiquido;
    }

    /**
     *
     * @param valorLiquido
     */
    public void setValorLiquido(long valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

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
        this.fornecedor = fornecedor;
    }

    /**
     *
     * @return
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     *
     * @param cnpj
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     *
     * @return
     */
    public boolean isClassificada() {
        return classificada;
    }

    /**
     *
     * @param classificada
     */
    public void setClassificada(boolean classificada) {
        this.classificada = classificada;
    }

    /**
     *
     * @return
     */
    public Date getFluxo() {
        return fluxo;
    }

    /**
     *
     * @param fluxo
     */
    public void setFluxo(Date fluxo) {
        this.fluxo = fluxo;
    }

    /**
     *
     * @return
     */
    public Date getVencimentoNota() {
        return vencimentoNota;
    }

    /**
     *
     * @param vencimentoNota
     */
    public void setVencimentoNota(Date vencimentoNota) {
        this.vencimentoNota = vencimentoNota;
    }

    
}
