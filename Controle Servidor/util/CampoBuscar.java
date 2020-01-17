/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author Eduardo
 */
public enum CampoBuscar {

    /**
     *
     */
    INSTANCIA("","","",false),

    /**
     *
     */
    NOTANUMERO("NOTA", "Numero", "NUMERO", true),

    /**
     *
     */
    NOTAFORNECEDORNOME("NOTA", "Razao Social", "RAZAOSOCIAL", false),

    /**
     *
     */
    NOTAFORNECEDORCNPJ("NOTA", "CNPJ", "CNPJ", false),

    /**
     *
     */
    CONTRATOFORNECEDORNOME("CONTRATO", "Razao Social", "RAZAOSOCIAL", false),

    /**
     *
     */
    CONTRATOFORNECEDORCNPJ("CONTRATO", "CNPJ", "CNPJ", true),

    /**
     *
     */
    CONTRATONUMERO("CONTRATO", "Numero", "NUMERO", false),

    /**
     *
     */
    CONTRATODESCRICAO("CONTRATO", "Descrição", "DESCRICAO", false),

    /**
     *
     */
    FORNECEDORRAZAOSOCIAL("FORNECEDOR", "Razao Social", "RAZAOSOCIAL", false),

    /**
     *
     */
    FORNECEDORNOMEFANTASIA("FORNECEDOR", "Nome Fantasia", "NOMEFANTASIA", false),

    /**
     *
     */
    FORNECEDORCNPJ("FORNECEDOR", "CNPJ", "CNPJ", true);

    private String tabela, campo, logica;
    private boolean padrao;

    CampoBuscar(String tabela, String campo, String logica, boolean padrao) {
        this.tabela = tabela;
        this.campo = campo;
        this.logica = logica;
        this.padrao = padrao;
    }

    /**
     *
     * @return
     */
    public String getTabela() {
        return tabela;
    }

    /**
     *
     * @param tabela
     */
    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    /**
     *
     * @return
     */
    public String getCampo() {
        return campo;
    }

    /**
     *
     * @param campo
     */
    public void setCampo(String campo) {
        this.campo = campo;
    }

    /**
     *
     * @return
     */
    public String getLogica() {
        return logica;
    }

    /**
     *
     * @param logica
     */
    public void setLogica(String logica) {
        this.logica = logica;
    }

    /**
     *
     * @return
     */
    public boolean isPadrao() {
        return padrao;
    }

    /**
     *
     * @param padrao
     */
    public void setPadrao(boolean padrao) {
        this.padrao = padrao;
    }

    /**
     *
     * @param tabela
     * @return
     */
    public String campoPadrao(String tabela) {
        for (CampoBuscar a : values()) {
            if (a.getTabela().equals(tabela)) {
                if (a.padrao) {
                    return a.campo;
                }
            }
        }
        return "";
    }
}
