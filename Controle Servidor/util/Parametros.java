/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.Date;

/**
 * @author Guilherme
 */
public class Parametros{

    private String fornecedor = "";
    private Date emissaoDe = new Date(0), emissaoAte = new Date(0), vencimentoDe = new Date(0), vencimentoAte = new Date(0);
    private long valorDe, valorAte;
    private boolean classificada, all;

    /**
     *
     * @param emissaoDe
     * @param emissaoAte
     * @param vencimentoDe
     * @param vencimentoAte
     * @param fornecedor
     * @param valorDe
     * @param valorAte
     * @param classificada
     * @param all
     */
    public Parametros(Date emissaoDe, Date emissaoAte, Date vencimentoDe, Date vencimentoAte, String fornecedor, long valorDe, long valorAte, boolean classificada, boolean all) {
        this.emissaoDe = emissaoDe;
        this.emissaoAte = emissaoAte;
        this.vencimentoDe = vencimentoDe;
        this.vencimentoAte = vencimentoAte;
        this.fornecedor = fornecedor;
        this.valorDe = valorDe;
        this.valorAte = valorAte;
        this.classificada = classificada;
        this.all = all;
    }

    /**
     *
     */
    public Parametros() {

    }

    /**
     *
     * @return
     */
    public Date getEmissaoDe() {
        return emissaoDe;
    }

    /**
     *
     * @param emissaoDe
     */
    public void setEmissaoDe(Date emissaoDe) {

        this.emissaoDe = emissaoDe;
    }

    /**
     *
     * @return
     */
    public Date getEmissaoAte() {
        return emissaoAte;
    }

    /**
     *
     * @param emissaoAte
     */
    public void setEmissaoAte(Date emissaoAte) {
        this.emissaoAte = emissaoAte;
    }

    /**
     *
     * @return
     */
    public Date getVencimentoDe() {
        return vencimentoDe;
    }

    /**
     *
     * @param vencimentoDe
     */
    public void setVencimentoDe(Date vencimentoDe) {
        this.vencimentoDe = vencimentoDe;
    }

    /**
     *
     * @return
     */
    public Date getVencimentoAte() {
        return vencimentoAte;
    }

    /**
     *
     * @param vencimentoAte
     */
    public void setVencimentoAte(Date vencimentoAte) {
        this.vencimentoAte = vencimentoAte;
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
    public long getValorDe() {
        return valorDe;
    }

    /**
     *
     * @param valorDe
     */
    public void setValorDe(long valorDe) {
        this.valorDe = valorDe;
    }

    /**
     *
     * @return
     */
    public long getValorAte() {
        return valorAte;
    }

    /**
     *
     * @param valorAte
     */
    public void setValorAte(long valorAte) {
        this.valorAte = valorAte;
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
    public boolean isAll() {
        return all;
    }

    /**
     *
     * @param all
     */
    public void setAll(boolean all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return emissaoDe + ":" + emissaoAte + ":" + vencimentoDe + ":" + vencimentoAte + ":" + fornecedor + ":"
                + valorDe + ":" + valorAte + ":" + classificada + ":" + all + ":";
    }

}
