/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Fornecedor;

import Util.Dado;

/**
 * @author Guilherme
 */
public class Fornecedor extends Dado {

    private String razaoSocial = "", nomeFantasia = "", sigla = "", tipoEmpresa = "", cnpj = "", inscEstadual = "", inscMunicipal = "", natureza = "";

    /**
     *
     * @return
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     *
     * @param razaoSocial
     */
    public void setRazaoSocial(String razaoSocial) {
        if(razaoSocial == null)
            razaoSocial = "";
        this.razaoSocial = razaoSocial.toUpperCase();
    }

    /**
     *
     * @return
     */
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    /**
     *
     * @param nomeFantasia
     */
    public void setNomeFantasia(String nomeFantasia) {
        if(nomeFantasia == null)
            nomeFantasia = "";
        this.nomeFantasia = nomeFantasia.toUpperCase();
    }

    /**
     *
     * @return
     */
    public String getSigla() {
        return sigla;
    }

    /**
     *
     * @param sigla
     */
    public void setSigla(String sigla) {
        if(sigla == null)
            sigla = "";
        this.sigla = sigla.toUpperCase();
    }

    /**
     *
     * @return
     */
    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    /**
     *
     * @param tipoEmpresa
     */
    public void setTipoEmpresa(String tipoEmpresa) {
        if(tipoEmpresa == null)
            tipoEmpresa = "";
        this.tipoEmpresa = tipoEmpresa;
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
        if(cnpj == null)
            cnpj = "";
        this.cnpj = cnpj;
    }

    /**
     *
     * @return
     */
    public String getInscEstadual() {
        return inscEstadual;
    }

    /**
     *
     * @param inscEstadual
     */
    public void setInscEstadual(String inscEstadual) {
        if(inscEstadual == null)
            inscEstadual = "";
        this.inscEstadual = inscEstadual;
    }

    /**
     *
     * @return
     */
    public String getInscMunicipal() {
        return inscMunicipal;
    }

    /**
     *
     * @param inscMunicipal
     */
    public void setInscMunicipal(String inscMunicipal) {
        if(inscMunicipal == null)
            inscMunicipal = "";
        this.inscMunicipal = inscMunicipal;
    }

    /**
     *
     * @return
     */
    public String getNatureza() {
        return natureza;
    }

    /**
     *
     * @param natureza
     */
    public void setNatureza(String natureza) {
        if(natureza == null)
            natureza = "";
        this.natureza = natureza;
    }

}
